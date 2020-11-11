package io.rackshift.strategy.statemachine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ExecutionLogConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.Task;
import io.rackshift.mybatis.domain.TaskWithBLOBs;
import io.rackshift.service.ExecutionLogService;
import io.rackshift.service.TaskService;
import io.rackshift.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateMachine {
    @Resource
    private ExecutionLogService executionLogService;
    @Resource
    private TaskService taskService;
    private ConcurrentHashMap<LifeEventType, IStateHandler> handlerMap = new ConcurrentHashMap<>();
    private static List<String> endStatusList = new ArrayList<String>() {{
        add(ServiceConstants.TaskStatusEnum.failed.name());
        add(ServiceConstants.TaskStatusEnum.succeeded.name());
    }};

    public void configureHandler(LifeEventType event, IStateHandler handler) {
        handlerMap.put(event, handler);
    }

    public void runTaskList(List<TaskWithBLOBs> tasks) {
        tasks.forEach(task -> {
            LifeEvent event = buildEvent(task);
            String taskId = event.getWorkflowRequestDTO().getTaskId();
            if (task != null && ServiceConstants.TaskStatusEnum.created.name().equals(task.getStatus())) {
                Task preTask = taskService.getById(task.getPreTaskId());
                //等待前置任务处理结束才能执行
                if (preTask == null || (preTask != null && endStatusList.contains(preTask.getStatus()))) {
                    task.setStatus(ServiceConstants.TaskStatusEnum.running.name());
                    taskService.update(task);
                    executionLogService.saveLogDetail(taskId, task.getUserId(), ExecutionLogConstants.OperationEnum.START.name(), event.getBareMetalId(), String.format("开始执行:%s:worflow:%s", event.getEventType().getDesc(), Optional.ofNullable(event.getWorkflowRequestDTO().getWorkflowName()).orElse("无")));
                    if (handlerMap.get(event.getEventType()) != null) {
                        handlerMap.get(event.getEventType()).handle(event, taskId, task.getUserId());
                    } else {
                        LogUtil.error(String.format("不支持的事件类型！%s", JSON.toJSONString(event)));
                    }
                }
            }
        });
    }

    public void sendEvent(LifeEvent event) {
        handlerMap.get(event.getEventType()).handleNoSession(event);
    }

    private LifeEvent buildEvent(TaskWithBLOBs task) {
        LifeEventType type = LifeEventType.fromStartType(task.getWorkFlowId());
        WorkflowRequestDTO requestDTO = new WorkflowRequestDTO();
        requestDTO.setBareMetalId(task.getBareMetalId());
        requestDTO.setTaskId(task.getId());
        requestDTO.setWorkflowName(task.getWorkFlowId());
        requestDTO.setParams(JSONObject.parseObject(Optional.ofNullable(task.getParams()).orElse("{}")));
        return LifeEvent.builder().withEventType(type).withWorkflowRequestDTO(requestDTO);
    }

}
