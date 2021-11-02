package io.rackshift.strategy.statemachine.handler;

import io.rackshift.engine.util.CatalogParser;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.MachineEntity;
import io.rackshift.strategy.statemachine.*;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEventType.POST_DISCOVERY_WORKFLOW_END)
public class DiscoveryEndHandler extends AbstractHandler {
    @Resource
    private CatalogParser catalogParser;
    @Resource
    private BareMetalManager bareMetalManager;
    @Override
    public void handleYourself(LifeEvent event) {
        extractCatalogs(event);
        changeStatus(event, LifeStatus.ready, false);
    }

    /**
     * //:todo
     * @param event
     */
    private void extractCatalogs(LifeEvent event) {
        MachineEntity en = catalogParser.parse(event);
        try {
            bareMetalManager.saveOrUpdateEntity(en);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
