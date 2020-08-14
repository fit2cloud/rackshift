package io.rackshift.strategy.statemachine;

public interface IStateHandler {

    void handle(LifeEvent event, String executionId, String user);

    void revert(LifeEvent event, String executionId, String user);
}
