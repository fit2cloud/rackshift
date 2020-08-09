package io.rackshift.manager;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ThreadPoolManager {

    private ExecutorService executors = Executors.newFixedThreadPool(10);

    public void addTask(Thread t) {
        executors.submit(t);
    }
}
