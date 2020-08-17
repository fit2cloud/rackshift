package io.rackshift.strategy.statemachine.handler;

import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.RSException;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.service.OutBandService;
import io.rackshift.service.RackHDService;
import io.rackshift.strategy.statemachine.*;
import io.rackshift.utils.Translator;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEventType.FILL_OBMS)
public class OBMHandler extends AbstractHandler {
    @Resource
    private RackHDService rackHDService;
    @Resource
    private OutBandService outBandService;
    @Resource
    private BareMetalManager bareMetalManager;

    @Override
    public void handleYourself(LifeEvent event) {
        WorkflowRequestDTO requestDTO = event.getWorkflowRequestDTO();
        BareMetal bareMetal = getBareMetalById(requestDTO.getBareMetalId());
        String curStatus = bareMetal.getStatus();
        OutBand o = requestDTO.getParams().getObject("outband", OutBand.class);
        if (o == null) {
            RSException.throwExceptions(Translator.get("i18n_param_error"));
        }
        boolean result = rackHDService.createOrUpdateObm(o, bareMetal, bareMetal.getServerId());
        if (result) {
            outBandService.saveOrUpdate(o);
        } else {
            RSException.throwExceptions(Translator.get("i18n_state_error"));
        }

        if (LifeStatus.onrack.name().equals(curStatus)) {
            bareMetal.setStatus(LifeStatus.ready.name());
            bareMetalManager.update(bareMetal, true);
        } else {
            bareMetalManager.update(bareMetal, false);
        }
    }
}
