package io.rackshift.strategy.statemachine.handler;

import io.rackshift.strategy.statemachine.*;

@EventHandlerAnnotation(LifeEventType.POST_OS_WORKFLOW_END)
public class OsWorkflowEndHandler extends AbstractHandler {
    @Override
    public void handleYourself(LifeEvent event) {
        changeStatus(event, LifeStatus.deployed, false);
    }
}
