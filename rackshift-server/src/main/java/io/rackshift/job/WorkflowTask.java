package io.rackshift.job;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ExecutionLogConstants;
import io.rackshift.service.ExecutionLogService;
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

    public WorkflowTask(String user, LifeEvent event, IStateHandler handler, ExecutionLogService executionLogService, WorkflowTask preTask) {
        this.user = user;
        this.event = event;
        this.handler = handler;
        this.executionLogService = executionLogService;
        this.preTask = preTask;
    }

    public void setPreTask(WorkflowTask preTask) {
        this.preTask = preTask;
    }

    @Override
    public void run() {
        if (preTask != null) {
            try {
                preTask.join();
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
        String executionLogId = executionLogService.saveLog(ExecutionLogConstants.SUBMIT).getId();
        executionLogService.saveLogDetail(executionLogId, user, ExecutionLogConstants.OperationEnum.START.name(), event.getBareMetalId(), null, String.format("执行event:%s:worflow:%s,参数:%s", event.getEventType().getDesc(), Optional.ofNullable(event.getWorkflowRequestDTO().getWorkflowName()).orElse("无"), (Optional.ofNullable(event.getWorkflowRequestDTO().getParams()).orElse(new JSONObject())).toJSONString()));
        handler.handle(event, executionLogId, user);
        executionLogService.saveLogDetail(executionLogId, user, ExecutionLogConstants.OperationEnum.END.name(), event.getBareMetalId(), null, String.format("执行event:%s:worflow:%s,参数:%s", event.getEventType().getDesc(), Optional.ofNullable(event.getWorkflowRequestDTO().getWorkflowName()).orElse("无"), (Optional.ofNullable(event.getWorkflowRequestDTO().getParams()).orElse(new JSONObject())).toJSONString()));
    }
}
