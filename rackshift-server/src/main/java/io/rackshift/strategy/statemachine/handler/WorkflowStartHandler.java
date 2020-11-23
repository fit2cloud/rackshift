package io.rackshift.strategy.statemachine.handler;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.config.WorkflowConfig;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.Task;
import io.rackshift.service.RackHDService;
import io.rackshift.service.TaskService;
import io.rackshift.strategy.statemachine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEventType.POST_OTHER_WORKFLOW_START)
public class WorkflowStartHandler extends AbstractHandler {
    @Resource
    private RackHDService rackHDService;
    @Resource
    private TaskService taskService;

    @Override
    public void handleYourself(LifeEvent event) {
        changeStatus(event, LifeStatus.provisioning, true);
        String taskId = event.getWorkflowRequestDTO().getTaskId();
        Task task = taskService.getById(taskId);
        task.setStatus(ServiceConstants.TaskStatusEnum.running.name());
        taskService.update(task);
        WorkflowRequestDTO requestDTO = event.getWorkflowRequestDTO();
        JSONObject params = requestDTO.getParams();
        BareMetal bareMetal = getBareMetalById(requestDTO.getBareMetalId());
        if (params == null) {
            revert(event);
        }

        String instanceId = rackHDService.postWorkflowNoWait(WorkflowConfig.geRackhdUrlById(bareMetal.getEndpointId()), bareMetal.getServerId(), requestDTO.getWorkflowName(), params);
        task.setStatus(ServiceConstants.TaskStatusEnum.running.name());
        task.setInstanceId(instanceId);
        taskService.update(task);
        notifyWebSocket();
    }
}
