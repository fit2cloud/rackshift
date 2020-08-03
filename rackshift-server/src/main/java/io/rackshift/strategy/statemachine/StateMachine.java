package io.rackshift.strategy.statemachine;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateMachine {

    private ConcurrentHashMap<LifeEvent, IStateHandler> handlerMap = new ConcurrentHashMap<>();

    public void configureHandler(LifeEvent event, IStateHandler handler) {
        handlerMap.put(event, handler);
    }

    public void sendEvent(LifeEvent event) {
        handlerMap.get(event).handle(event);
    }
}
