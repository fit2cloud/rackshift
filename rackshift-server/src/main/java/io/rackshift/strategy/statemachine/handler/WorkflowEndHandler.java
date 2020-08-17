package io.rackshift.strategy.statemachine.handler;

import io.rackshift.strategy.statemachine.*;

@EventHandlerAnnotation(LifeEventType.POST_OTHER_WORKFLOW_END)
public class WorkflowEndHandler extends AbstractHandler {
    @Override
    public void handleYourself(LifeEvent event) {
        changeStatus(event, LifeStatus.allocated, false);
    }
}
