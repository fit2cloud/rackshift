package io.rackshift.strategy.statemachine;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ExecutionLogConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.RSException;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.service.ExecutionLogService;
import io.rackshift.utils.ExceptionUtils;
import io.rackshift.utils.Translator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public abstract class AbstractHandler implements IStateHandler {
    @Resource
    private BareMetalManager bareMetalManager;
    @Resource
    protected ExecutionLogService executionLogService;

    protected BareMetal getBareMetalById(String id) {
        return bareMetalManager.getBareMetalById(id);
    }

    protected List<LifeStatus> preStatus = new ArrayList<LifeStatus>() {{
        add(LifeStatus.ready);
        add(LifeStatus.allocated);
        add(LifeStatus.deployed);
    }};

    protected ThreadLocal<Map<String, String>> executionMap = new ThreadLocal<>();

    protected String getExecutionId() {
        return executionMap.get().get("executionId");
    }

    protected String getUser() {
        return executionMap.get().get("user");
    }

    protected String getBeforeChangeStatus() {
        return executionMap.get().get("beforeChangeStatus");
    }

    public ThreadLocal<Map<String, String>> getExecutionMap() {
        return executionMap;
    }

    public abstract void handleYourself(LifeEvent event) throws Exception;

    @Override
    public void handleNoSession(LifeEvent event) {
        try {
            handleYourself(event);
        } catch (Exception e) {
        }
    }

    @Override
    public void handle(LifeEvent event, String executionId, String user) {
        BareMetal bareMetal = getBareMetalById(event.getWorkflowRequestDTO().getBareMetalId());
        Map<String, String> statusMap = new HashMap();
        statusMap.put("beforeChangeStatus", bareMetal.getStatus());
        statusMap.put("executionId", executionId);
        statusMap.put("user", user);
        getExecutionMap().set(statusMap);
        System.out.println("handing" + bareMetal.getMachineModel());
        try {
            handleYourself(event);
        } catch (Exception e) {
            executionLogService.error(executionId);
            executionLogService.saveLogDetail(executionId, user, ExecutionLogConstants.OperationEnum.ERROR.name(), event.getBareMetalId(), null, String.format("错误：%s", ExceptionUtils.getExceptionDetail(e)));
            revert(event, getExecutionId(), getUser());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void revert(LifeEvent event, String executionId, String user) {
        executionLogService.saveLogDetail(executionId, user, ExecutionLogConstants.OperationEnum.ERROR.name(), event.getBareMetalId(), null, String.format("错误：event:%s:worflow:%s,参数:%s,回滚状态至%s", event.getEventType().getDesc(), Optional.ofNullable(event.getWorkflowRequestDTO().getWorkflowName()).orElse("无"), (Optional.ofNullable(event.getWorkflowRequestDTO().getParams()).orElse(new JSONObject())).toJSONString(), getExecutionMap().get().get("beforeChangeStatus")));
        changeStatus(event, LifeStatus.valueOf(getExecutionMap().get().get("beforeChangeStatus")), false);
    }

    protected void beforeChange(LifeStatus curStatus) {
        if (!preStatus.contains(curStatus)) RSException.throwExceptions(Translator.get("i18n_status_not_valid"));
    }

    protected void changeStatus(LifeEvent event, LifeStatus status, boolean needCheckStatus) {
        WorkflowRequestDTO requestDTO = event.getWorkflowRequestDTO();
        BareMetal bareMetal = getBareMetalById(requestDTO.getBareMetalId());
        if (needCheckStatus) {
            beforeChange(LifeStatus.valueOf(bareMetal.getStatus()));
        }
        bareMetal.setStatus(status.name());
        bareMetalManager.update(bareMetal, true);
    }
}
