package io.rackshift.strategy.statemachine;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.job.WorkflowTask;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.TaskWithBLOBs;
import io.rackshift.service.ExecutionLogService;
import io.rackshift.service.TaskService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateMachine {
    @Resource
    private ExecutionLogService executionLogService;
    @Resource
    private TaskService taskService;
    private ConcurrentHashMap<LifeEventType, IStateHandler> handlerMap = new ConcurrentHashMap<>();

    public void configureHandler(LifeEventType event, IStateHandler handler) {
        handlerMap.put(event, handler);
    }

    public void sendTaskListAsyn(List<TaskWithBLOBs> tasks) {
        List<LifeEvent> events = new LinkedList<>();
        tasks.forEach(task -> {
            LifeEventType type = LifeEventType.fromStartType(task.getWorkFlowId());
            WorkflowRequestDTO requestDTO = new WorkflowRequestDTO();
            requestDTO.setBareMetalId(task.getBareMetalId());
            requestDTO.setTaskId(task.getId());
            requestDTO.setWorkflowName(task.getWorkFlowId());
            requestDTO.setParams(JSONObject.parseObject(task.getParams()));
            LifeEvent lifeEvent = LifeEvent.builder().withEventType(type).withWorkflowRequestDTO(requestDTO);
            events.add(lifeEvent);
        });
        List<WorkflowTask> workflowTasks = buildTaskQueue(events);
        workflowTasks.forEach(t -> {
            t.start();
        });
    }

    public void sendEvent(LifeEvent event) {
        handlerMap.get(event.getEventType()).handleNoSession(event);
    }

    private List<WorkflowTask> buildTaskQueue(List<LifeEvent> events) {
        List<WorkflowTask> workflowTasks = new LinkedList<>();

        events.forEach(e -> {
            workflowTasks.add(
                    new WorkflowTask(e, handlerMap.get(e.getEventType()), executionLogService, null, taskService)
            );
        });

        for (int i = 0; i < workflowTasks.size(); i++) {
            if (i != 0) workflowTasks.get(i).setPreTask(workflowTasks.get(i - 1));
        }

        return workflowTasks;
    }


}
