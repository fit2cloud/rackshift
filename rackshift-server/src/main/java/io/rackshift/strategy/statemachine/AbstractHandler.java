package io.rackshift.strategy.statemachine;

import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.RSException;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.utils.Translator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public abstract class AbstractHandler implements IStateHandler {
    @Resource
    private BareMetalManager bareMetalManager;

    protected BareMetal getBareMetalById(String id) {
        return bareMetalManager.getBareMetalById(id);
    }

    protected List<LifeStatus> preStatus = new ArrayList<LifeStatus>() {{
        add(LifeStatus.ready);
        add(LifeStatus.allocated);
        add(LifeStatus.deployed);
    }};

    @Override
    public void handle(LifeEvent event) {
        handleMyself(event);
    }

    protected void beforeChange(LifeStatus curStatus) {
        if (!preStatus.contains(curStatus)) RSException.throwExceptions(Translator.get("i18n_status_not_valid"));
    }

    protected void changeStatus(LifeEvent event, LifeStatus status, boolean needCheckStatus) {
        WorkflowRequestDTO requestDTO = event.getParams();
        BareMetal bareMetal = getBareMetalById(requestDTO.getBareMetalId());
        if (needCheckStatus) {
            beforeChange(LifeStatus.valueOf(bareMetal.getStatus()));
        }
        bareMetal.setStatus(status.name());
        bareMetalManager.update(bareMetal, true);
    }
}
