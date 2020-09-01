package io.rackshift.strategy.statemachine.handler;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.Application;
import io.rackshift.config.WorkflowConfig;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.service.RackHDService;
import io.rackshift.strategy.statemachine.*;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEventType.POST_OTHER_WORKFLOW_START)
public class WorkflowStartHandler extends AbstractHandler {
    @Resource
    private RackHDService rackHDService;
    @Resource
    private String rackhdUrl;

    @Override
    public void handleYourself(LifeEvent event) {
        changeStatus(event, LifeStatus.provisioning, true);

        //下发装机workflow
        WorkflowRequestDTO requestDTO = event.getWorkflowRequestDTO();
        JSONObject params = requestDTO.getParams();
        BareMetal bareMetal = getBareMetalById(requestDTO.getBareMetalId());
        if (params == null) {
            revert(event, getExecutionId(), getUser());
        }

        boolean result = rackHDService.postWorkflow(WorkflowConfig.geRrackhdUrl(bareMetal.getEndpointId()), bareMetal.getServerId(), requestDTO.getWorkflowName(), params);
        if (result) {
            changeStatus(event, LifeStatus.allocated, false);
        } else {
            revert(event, getExecutionId(), getUser());
        }
    }
}
