package io.rackshift.strategy.statemachine.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.config.WorkflowConfig;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.service.RackHDService;
import io.rackshift.strategy.statemachine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEventType.POST_OS_WORKFLOW_START)
public class OsWorkflowStartHandler extends AbstractHandler {

    @Autowired
    private SimpMessagingTemplate template;
    @Resource
    private RackHDService rackHDService;
    @Resource
    private BareMetalManager bareMetalManager;

    @Override
    public void handleYourself(LifeEvent event) {
        changeStatus(event, LifeStatus.deploying, true);

        //下发装机workflow
        WorkflowRequestDTO requestDTO = event.getWorkflowRequestDTO();
        JSONObject params = requestDTO.getParams();
        JSONObject extraParams = requestDTO.getExtraParams();
        BareMetal bareMetal = getBareMetalById(requestDTO.getBareMetalId());
        if (params == null) {
            revert(event, getExecutionId(), getUser());
        }
        if (extraParams != null) {
            if (extraParams.containsKey("unit") && "GB".equalsIgnoreCase(extraParams.getString("unit"))) {
                params = setPartitionSize(params);
            }
        }

        boolean result = rackHDService.postWorkflow(WorkflowConfig.geRackhdUrlById(bareMetal.getEndpointId()), bareMetal.getServerId(), requestDTO.getWorkflowName(), params);
        if (result) {
            changeStatus(event, LifeStatus.deployed, false);
            bareMetal.setStatus(null);
            bareMetal.setIpArray(params.getJSONObject("options").getJSONObject("defaults").getJSONArray("networkDevices").getJSONObject(0).getJSONObject("ipv4").getString("ipAddr"));
            bareMetalManager.update(bareMetal, false);
        } else {
            revert(event, getExecutionId(), getUser());
        }
        template.convertAndSend("/topic/lifecycle", "");
    }

    private JSONObject setPartitionSize(JSONObject params) {
        JSONObject options = params.getJSONObject("options");
        JSONObject defaults = options.getJSONObject("defaults");
        JSONArray partitions = defaults.getJSONArray("installPartitions");
        for (int i = 0; i < partitions.size(); i++) {
            JSONObject p = partitions.getJSONObject(i);
            if (!p.getString("size").equalsIgnoreCase("auto") && !"biosboot".equalsIgnoreCase(p.getString("mountPoint"))) {
                p.put("size", Integer.valueOf(((JSONObject) p).getString("size")) * 1024 + "");
            }
            partitions.set(i, p);
        }
        defaults.put("installPartitions", partitions);
        options.put("defaults", defaults);
        params.put("options", options);
        return params;
    }

}
