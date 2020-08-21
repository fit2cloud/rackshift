package io.rackshift.strategy.statemachine;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ExecutionLogConstants;
import io.rackshift.job.WorkflowTask;
import io.rackshift.manager.WorkflowThreadPoolManager;
import io.rackshift.service.ExecutionLogService;
import io.rackshift.utils.SessionUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateMachine {
    @Resource
    private WorkflowThreadPoolManager threadPoolManager;
    @Resource
    private ExecutionLogService executionLogService;
    private ConcurrentHashMap<LifeEventType, IStateHandler> handlerMap = new ConcurrentHashMap<>();

    public void configureHandler(LifeEventType event, IStateHandler handler) {
        handlerMap.put(event, handler);
    }

    public void sendEventAsyn(LifeEvent event) {
        threadPoolManager.addTask(new WorkflowTask(SessionUtil.getUser().getId(), event, handlerMap.get(event.getEventType()), executionLogService));
    }

    public void sendEvent(LifeEvent event) {
        String executionLogId = executionLogService.saveLog(ExecutionLogConstants.SUBMIT).getId();
        executionLogService.saveLogDetail(executionLogId, SessionUtil.getUser().getId(), ExecutionLogConstants.OperationEnum.START.name(), event.getBareMetalId(), null, String.format("执行event:%s:worflow:%s,参数:%s", event.getEventType().getDesc(), Optional.ofNullable(event.getWorkflowRequestDTO().getWorkflowName()).orElse("无"), (Optional.ofNullable(event.getWorkflowRequestDTO().getParams()).orElse(new JSONObject())).toJSONString()));
        handlerMap.get(event.getEventType()).handle(event, executionLogId, SessionUtil.getUser().getId());
        executionLogService.saveLogDetail(executionLogId, SessionUtil.getUser().getId(), ExecutionLogConstants.OperationEnum.END.name(), event.getBareMetalId(), null, String.format("执行event:%s:worflow:%s,参数:%s", event.getEventType().getDesc(), Optional.ofNullable(event.getWorkflowRequestDTO().getWorkflowName()).orElse("无"), (Optional.ofNullable(event.getWorkflowRequestDTO().getParams()).orElse(new JSONObject())).toJSONString()));
        executionLogService.finish(executionLogId);
    }

}
