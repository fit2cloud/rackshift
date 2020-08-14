package io.rackshift.strategy.statemachine.handler;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ExecutionLogConstants;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.service.RackHDService;
import io.rackshift.strategy.statemachine.*;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEvent.POST_OS_WORKFLOW_START)
public class OsWorkflowStartHandler extends AbstractHandler {

    @Resource
    private RackHDService rackHDService;
    @Resource
    private String rackhdUrl;
    @Resource
    private StateMachine stateMachine;

    @Override
    public void handleYourself(LifeEvent event) {
        changeStatus(event, LifeStatus.deploying, true);

        //下发装机workflow
        WorkflowRequestDTO requestDTO = event.getWorkflowRequestDTO();
        JSONObject params = requestDTO.getParams();
        BareMetal bareMetal = getBareMetalById(requestDTO.getBareMetalId());
        if (params == null) {
            revert(event, getExecutionId(), getUser());
        }

        boolean result = rackHDService.postWorkflow(rackhdUrl, bareMetal.getServerId(), requestDTO.getWorkflowName(), params);
        if (result) {
            changeStatus(event, LifeStatus.deployed, false);
        } else {
            revert(event, getExecutionId(), getUser());
        }
    }
}
