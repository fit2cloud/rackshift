package io.rackshift.job;

import io.rackshift.constants.ExecutionLogConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.mybatis.domain.Task;
import io.rackshift.service.ExecutionLogService;
import io.rackshift.service.TaskService;
import io.rackshift.strategy.statemachine.IStateHandler;
import io.rackshift.strategy.statemachine.LifeEvent;

import java.util.Optional;

/**
 * workflow工作线程
 */
public class WorkflowTask extends Thread {
    private String user;
    private LifeEvent event;
    private IStateHandler handler;
    private ExecutionLogService executionLogService;
    private WorkflowTask preTask;
    private TaskService taskService;

    public WorkflowTask(LifeEvent event, IStateHandler handler, ExecutionLogService executionLogService, WorkflowTask preTask, TaskService taskService) {
        this.event = event;
        this.handler = handler;
        this.executionLogService = executionLogService;
        this.preTask = preTask;
        this.taskService = taskService;
        this.user = taskService.getById(event.getWorkflowRequestDTO().getTaskId()).getUserId();
    }

    public void setPreTask(WorkflowTask preTask) {
        this.preTask = preTask;
    }

    @Override
    public void run() {
        String taskId = event.getWorkflowRequestDTO().getTaskId();
        Task task = taskService.getById(taskId);
        if (task != null && ServiceConstants.TaskStatusEnum.created.name().equals(task.getStatus())) {
            if (preTask != null) {
                try {
                    preTask.join();
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    // join异常才回滚workflow
                    executionLogService.saveLogDetail(taskId, user, ExecutionLogConstants.OperationEnum.ERROR.name(), event.getBareMetalId(), null, String.format("执行event:%s:worflow:%s,前置任务失败！终止执行！", event.getEventType().getDesc(), Optional.ofNullable(event.getWorkflowRequestDTO().getWorkflowName()).orElse("无")));
                    return;
                }
            }
            task.setStatus(ServiceConstants.TaskStatusEnum.running.name());
            taskService.update(task);
            executionLogService.saveLogDetail(taskId, user, ExecutionLogConstants.OperationEnum.START.name(), event.getBareMetalId(), null, String.format("开始执行event:%s:worflow:%", event.getEventType().getDesc(), Optional.ofNullable(event.getWorkflowRequestDTO().getWorkflowName()).orElse("无")));
            handler.handle(event, taskId, user);
            executionLogService.saveLogDetail(taskId, user, ExecutionLogConstants.OperationEnum.END.name(), event.getBareMetalId(), null, String.format("结束执行event:%s:worflow:%s", event.getEventType().getDesc(), Optional.ofNullable(event.getWorkflowRequestDTO().getWorkflowName()).orElse("无")));
        }
    }
}
