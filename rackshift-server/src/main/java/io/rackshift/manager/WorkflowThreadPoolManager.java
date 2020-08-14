package io.rackshift.manager;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 只能用于workflow的执行线程池
 */
@Component
public class WorkflowThreadPoolManager {

    private ExecutorService executors = Executors.newFixedThreadPool(50);

    public void addTask(Runnable t) {
        executors.submit(t);
    }
}
