package io.rackshift.state.handler;

import io.rackshift.state.LifeEvent;

public interface StatusHandler {
    void handle(LifeEvent event);
}
