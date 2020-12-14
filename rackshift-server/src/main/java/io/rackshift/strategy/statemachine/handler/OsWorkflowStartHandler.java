package io.rackshift.strategy.statemachine.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.config.WorkflowConfig;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.Task;
import io.rackshift.service.RackHDService;
import io.rackshift.service.TaskService;
import io.rackshift.strategy.statemachine.*;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEventType.POST_OS_WORKFLOW_START)
public class OsWorkflowStartHandler extends AbstractHandler {


    @Resource
    private RackHDService rackHDService;
    @Resource
    private BareMetalManager bareMetalManager;
    @Resource
    private TaskService taskService;

    @Override
    public void handleYourself(LifeEvent event) {
        String taskId = event.getWorkflowRequestDTO().getTaskId();
        Task task = taskService.getById(taskId);

        //下发装机workflow
        WorkflowRequestDTO requestDTO = event.getWorkflowRequestDTO();
        JSONObject params = requestDTO.getParams();
        JSONObject extraParams = requestDTO.getExtraParams();
        BareMetal bareMetal = getBareMetalById(requestDTO.getBareMetalId());
        if (params == null) {
            revert(event);
        }
        if (extraParams != null) {
            if (extraParams.containsKey("unit") && "GB".equalsIgnoreCase(extraParams.getString("unit"))) {
                setPartitionSize(params);
            }

            if (!extraParams.containsKey("customPartition") || (extraParams.containsKey("customPartition") && !extraParams.getBoolean("customPartition"))) {
                removePartitions(params);
            }
        }

        String workflowId = rackHDService.postWorkflowNoWait(WorkflowConfig.geRackhdUrlById(bareMetal.getEndpointId()), bareMetal.getServerId(), requestDTO.getWorkflowName(), params);
        task.setStatus(ServiceConstants.TaskStatusEnum.running.name());
        task.setInstanceId(workflowId);
        taskService.update(task);
        changeStatus(event, LifeStatus.deploying, true);
    }

    private void removePartitions(JSONObject params) {
        params.getJSONObject("options").getJSONObject("defaults").remove("installPartitions");
    }

    private void setPartitionSize(JSONObject params) {
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
    }

}
