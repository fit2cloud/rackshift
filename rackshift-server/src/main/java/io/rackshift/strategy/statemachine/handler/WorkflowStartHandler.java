package io.rackshift.strategy.statemachine.handler;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.service.RackHDService;
import io.rackshift.strategy.statemachine.*;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEvent.POST_OTHER_WORKFLOW_START)
public class WorkflowStartHandler extends AbstractHandler {
    @Resource
    private RackHDService rackHDService;
    @Resource
    private String rackhdUrl;
    @Resource
    private StateMachine stateMachine;

    @Override
    public void handleYourself(LifeEvent event) {
        changeStatus(event, LifeStatus.provisioning, true);


        //下发装机workflow
        WorkflowRequestDTO requestDTO = event.getWorkflowRequestDTO();
        JSONObject params = requestDTO.getParams();
        if (params == null) {
            revert(event);
        }

        BareMetal bareMetal = getBareMetalById(requestDTO.getBareMetalId());
        boolean result = rackHDService.postWorkflow(rackhdUrl, bareMetal.getServerId(), requestDTO.getWorkflowName(), params);
        if (result) {
            LifeEvent event1 = LifeEvent.POST_OTHER_WORKFLOW_END;
            event1.setWorkflowRequestDTO(requestDTO);
            stateMachine.sendEvent(event1);
        } else {
            revert(event);
        }

    }
}
