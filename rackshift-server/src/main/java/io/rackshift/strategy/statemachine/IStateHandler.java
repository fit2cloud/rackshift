package io.rackshift.strategy.statemachine;

public interface IStateHandler {

    void handle(LifeEvent event);

    void handleNoSession(LifeEvent event);

    void revert(LifeEvent event);
}
