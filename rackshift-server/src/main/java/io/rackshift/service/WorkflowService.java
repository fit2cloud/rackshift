package io.rackshift.service;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.BasicDBObject;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.manager.WorkflowManager;
import io.rackshift.model.RSException;
import io.rackshift.model.ResultHolder;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.StateMachine;
import io.rackshift.utils.MongoUtil;
import io.rackshift.utils.Pager;
import io.rackshift.utils.Translator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class WorkflowService {
    @Resource
    private RackHDService rackHDService;
    @Resource
    private WorkflowManager workflowManager;
    @Resource
    private BareMetalManager bareMetalManager;
    @Resource
    private StateMachine stateMachine;

    public Pager<JSONArray> getGraphDefinitions(String name, int page, int pageSize) {
        String collections = "graphdefinitions";
        Pattern pattern = Pattern.compile(".*" + name + ".*", Pattern.CASE_INSENSITIVE);
        List<BasicDBObject> cond = new ArrayList<BasicDBObject>() {{
            add(new BasicDBObject("friendlyName", pattern));
            add(new BasicDBObject("injectableName", pattern));
        }};
        if (StringUtils.isNotBlank(name)) {
            return MongoUtil.page(collections, new BasicDBObject("$or", cond), page, pageSize);
        }
        return MongoUtil.page(collections, new BasicDBObject(), page, pageSize);
    }

    public ResultHolder getParamsByName(String name) {
        return ResultHolder.success(workflowManager.getParamsByName(name));
    }

    public ResultHolder run(List<WorkflowRequestDTO> requestDTOs) {
        if (requestDTOs.size() == 0) {
            return ResultHolder.success("");
        }
        for (WorkflowRequestDTO requestDTO : requestDTOs) {
            String bareMetalId = requestDTO.getBareMetalId();
            String workflowName = requestDTO.getWorkflowName();
            if (StringUtils.isAnyBlank(bareMetalId, workflowName)) {
                RSException.throwExceptions(Translator.get("i18n_error"));
            }
            BareMetal bareMetal = bareMetalManager.getBareMetalById(bareMetalId);
            if (bareMetal == null) {
                RSException.throwExceptions(Translator.get("i18n_error"));
            }
            LifeEvent event = LifeEvent.fromWorkflow(workflowName);
            event.setBareMetalId(bareMetalId);
            event.setWorkflowRequestDTO(requestDTO);
            stateMachine.sendEventAsyn(event);
        }
        return ResultHolder.success("");
    }

    public ResultHolder postParamsByName(WorkflowRequestDTO requestDTO) {
        return ResultHolder.success(workflowManager.saveWorkflowParamTemplate(requestDTO));
    }
}
