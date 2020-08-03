package io.rackshift.strategy.statemachine.handler;

import io.rackshift.strategy.statemachine.AbstractHandler;
import io.rackshift.strategy.statemachine.EventHandlerAnnotation;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeStatus;

@EventHandlerAnnotation(LifeEvent.POST_DISCOVERY_WORKFLOW_END)
public class DiscoveryEndHandler extends AbstractHandler {
    @Override
    public void handleMyself(LifeEvent event) {
        changeStatus(event, LifeStatus.ready, false);
    }
}
