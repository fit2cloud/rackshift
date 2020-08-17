package io.rackshift.strategy.statemachine.handler;

import io.rackshift.strategy.statemachine.*;

@EventHandlerAnnotation(LifeEventType.POST_DISCOVERY_WORKFLOW_END)
public class DiscoveryEndHandler extends AbstractHandler {
    @Override
    public void handleYourself(LifeEvent event) {
        changeStatus(event, LifeStatus.ready, false);
    }
}
