package io.rackshift.state;

import io.rackshift.state.handler.StatusHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateMachine {
    private ConcurrentHashMap<StatusEventTwo, LifeStatus> stateTransition = new ConcurrentHashMap<>();

    private ConcurrentHashMap<LifeStatus, StatusHandler> handlerMap = new ConcurrentHashMap<>();

    public LifeStatus getNextStatus(LifeStatus curStatus, LifeEvent event) {
        return stateTransition.get(new StatusEventTwo(curStatus, event));
    }

    public void configure(StatusEventTwo statusEventTwo, LifeStatus status) {
        stateTransition.put(statusEventTwo, status);
    }

    public void sendEvent(LifeStatus status, LifeEvent event) {
        handlerMap.get(status).handle(event);
    }
}
