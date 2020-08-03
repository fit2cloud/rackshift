package io.rackshift.strategy.statemachine;

public interface IStateHandler {

    void handle(LifeEvent event);

    void handleMyself(LifeEvent event);
}
