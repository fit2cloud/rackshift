package io.rackshift.strategy.statemachine;

import io.rackshift.manager.ThreadPoolManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateMachine {
    @Resource
    private ThreadPoolManager threadPoolManager;
    private ConcurrentHashMap<LifeEvent, IStateHandler> handlerMap = new ConcurrentHashMap<>();

    public void configureHandler(LifeEvent event, IStateHandler handler) {
        handlerMap.put(event, handler);
    }

    public void sendEventAsyn(LifeEvent event) {
        threadPoolManager.addTask(new Thread() {
            @Override
            public void run() {
                handlerMap.get(event).handle(event);
            }
        });

    }

    public void sendEvent(LifeEvent event) {
        handlerMap.get(event).handle(event);
    }
}
