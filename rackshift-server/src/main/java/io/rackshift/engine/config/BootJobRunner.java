package io.rackshift.engine.config;

import io.rackshift.mybatis.domain.Task;
import io.rackshift.service.TaskService;
import io.rackshift.strategy.statemachine.StateMachine;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class BootJobRunner implements ApplicationRunner {
    @Resource
    private TaskService taskService;
    @Resource
    private StateMachine stateMachine;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Task> runningTasks = taskService.getActiveTasks();
        runningTasks.forEach(t -> stateMachine.runTaskGraph(t.getId()));
    }
}
