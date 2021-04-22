package io.rackshift.strategy.statemachine.handler;

import io.rackshift.constants.ExecutionLogConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.TaskWithBLOBs;
import io.rackshift.mybatis.domain.Workflow;
import io.rackshift.service.TaskService;
import io.rackshift.service.WorkflowService;
import io.rackshift.strategy.statemachine.*;

import javax.annotation.Resource;
import java.util.Optional;

@EventHandlerAnnotation(LifeEventType.POST_OTHER_WORKFLOW_END)
public class WorkflowEndHandler extends AbstractHandler {
    @Resource
    private TaskService taskService;
    @Resource
    private BareMetalManager bareMetalManager;
    @Resource
    private WorkflowService workflowService;

    @Override
    public void handleYourself(LifeEvent event) {
        if (event.getWorkflowRequestDTO().getParams() == null) {
            return;
        }
        boolean result = event.getWorkflowRequestDTO().getParams().getBoolean("result");

        BareMetal bareMetal = getBareMetalById(event.getBareMetalId());
        TaskWithBLOBs task = taskService.getById(event.getWorkflowRequestDTO().getTaskId());
        Workflow workflow = workflowService.getById(task.getWorkFlowId());
        String workflowName = Optional.ofNullable(workflow).orElse(new Workflow()).getInjectableName();
        if (result) {
            bareMetal.setStatus(LifeStatus.allocated.name());
            task.setStatus(ServiceConstants.TaskStatusEnum.succeeded.name());
            executionLogService.saveLogDetail(task.getId(), task.getUserId(), ExecutionLogConstants.OperationEnum.END.name(), event.getBareMetalId(), String.format("裸金属服务器:%s,执行工作流 %s 成功！", bareMetal.getMachineModel() + " " + bareMetal.getMachineSn(), workflowName));
        } else {
            bareMetal.setStatus(LifeStatus.ready.name());
            task.setStatus(ServiceConstants.TaskStatusEnum.failed.name());
            executionLogService.saveLogDetail(task.getId(), task.getUserId(), ExecutionLogConstants.OperationEnum.END.name(), event.getBareMetalId(), String.format("裸金属服务器:%s,执行工作流 %s 失败！", bareMetal.getMachineModel() + " " + bareMetal.getMachineSn(), workflowName));
        }
        bareMetalManager.update(bareMetal, true);
        taskService.update(task);
        notifyWebSocket(bareMetal, task);
    }
}