package io.rackshift.strategy.ipmihandler;

import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.strategy.ipmihandler.base.CommandHandler;
import io.rackshift.strategy.ipmihandler.base.IPMICommandHandlerAnnotation;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerInterface;
import io.rackshift.utils.IPMIUtil;

@IPMICommandHandlerAnnotation(operation = "off", desc = "关机")
public class PowerOffHandler extends CommandHandler implements IPMIHandlerInterface {

    @Override
    public ResultHolder execute(IPMIUtil.Account account, BareMetal physicalMachine, OutBand outBand) {
        return execute(account, physicalMachine, outBand, "power off");
    }
}
