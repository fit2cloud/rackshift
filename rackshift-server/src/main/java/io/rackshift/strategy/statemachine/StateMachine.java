package io.rackshift.strategy.statemachine;

import io.rackshift.job.WorkflowTask;
import io.rackshift.service.ExecutionLogService;
import io.rackshift.utils.SessionUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateMachine {
    @Resource
    private ExecutionLogService executionLogService;
    private ConcurrentHashMap<LifeEventType, IStateHandler> handlerMap = new ConcurrentHashMap<>();

    public void configureHandler(LifeEventType event, IStateHandler handler) {
        handlerMap.put(event, handler);
    }

    public void sendEventListAsyn(List<LifeEvent> events) {
        List<WorkflowTask> workflowTasks = buildTaskQueue(events);
        workflowTasks.forEach(t -> {
            t.start();
        });
    }

    private List<WorkflowTask> buildTaskQueue(List<LifeEvent> events) {
        List<WorkflowTask> workflowTasks = new LinkedList<>();

        events.forEach(e -> {
            workflowTasks.add(
                    new WorkflowTask(SessionUtil.getUser().getId(), e, handlerMap.get(e.getEventType()), executionLogService, null)
            );
        });

        for (int i = 0; i < workflowTasks.size(); i++) {
            if (i != 0) workflowTasks.get(i).setPreTask(workflowTasks.get(i - 1));
        }

        return workflowTasks;
    }


}
