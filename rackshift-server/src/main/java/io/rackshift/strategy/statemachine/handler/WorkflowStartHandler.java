package io.rackshift.strategy.statemachine.handler;

import io.rackshift.mybatis.domain.TaskWithBLOBs;
import io.rackshift.service.TaskService;
import io.rackshift.strategy.statemachine.*;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEventType.POST_OTHER_WORKFLOW_START)
public class WorkflowStartHandler extends AbstractHandler {
    @Resource
    private TaskService taskService;

    @Override
    public void handleYourself(LifeEvent event) {
        String taskId = event.getWorkflowRequestDTO().getTaskId();
        TaskWithBLOBs task = taskService.getById(taskId);
        startTask(task);
        changeStatus(event, LifeStatus.provisioning, true);
    }
}
