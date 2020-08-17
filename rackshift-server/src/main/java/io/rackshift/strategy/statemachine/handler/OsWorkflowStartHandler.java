package io.rackshift.strategy.statemachine.handler;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.service.RackHDService;
import io.rackshift.strategy.statemachine.*;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEventType.POST_OS_WORKFLOW_START)
public class OsWorkflowStartHandler extends AbstractHandler {

    @Resource
    private RackHDService rackHDService;
    @Resource
    private String rackhdUrl;
    @Resource
    private BareMetalManager bareMetalManager;

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
            bareMetal.setStatus(null);
            bareMetal.setIpArray(params.getJSONObject("options").getJSONObject("defaults").getJSONArray("networkDevices").getJSONObject(0).getJSONObject("ipv4").getString("ipAddr"));
            bareMetalManager.update(bareMetal, false);
        } else {
            revert(event, getExecutionId(), getUser());
        }
    }

}
