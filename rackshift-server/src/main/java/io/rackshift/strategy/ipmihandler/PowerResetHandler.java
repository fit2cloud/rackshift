package io.rackshift.strategy.ipmihandler;

import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.service.OperationLogService;
import io.rackshift.strategy.ipmihandler.base.CommandHandler;
import io.rackshift.strategy.ipmihandler.base.IPMICommandHandlerAnnotation;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerInterface;
import io.rackshift.utils.IPMIUtil;
import io.rackshift.utils.SessionUtil;

import javax.annotation.Resource;

@IPMICommandHandlerAnnotation(operation = "reset", desc = "重启")
public class PowerResetHandler extends CommandHandler implements IPMIHandlerInterface {
    @Resource
    OperationLogService operationLogService;

    @Override
    public ResultHolder execute(IPMIUtil.Account account, BareMetal physicalMachine, OutBand outBand) {
        try {
            IPMIUtil.exeCommand(account, "power cycle");
            operationLogService.log(SessionUtil.getUser().getName(), "ipmi命令", "执行", account.getHost() + "执行命令:" + "power cycle" + " 成功！");
            return ResultHolder.success("");
        } catch (Exception e) {
            operationLogService.log(SessionUtil.getUser().getName(), "ipmi命令", "执行", account.getHost() + "执行命令:" + "power cycle" + " 失败！");
            return ResultHolder.error("ipmi命令调用失败！可能是网络不通或者账号密码错误或带外异常！");
        }
    }
}
