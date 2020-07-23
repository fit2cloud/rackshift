package io.rackshift.strategy.ipmihandler;

import io.rackshift.constants.RackHDConstants;
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

@IPMICommandHandlerAnnotation(operation = "pxe", desc = "通过pxe重启机器")
public class PxeHandler extends CommandHandler implements IPMIHandlerInterface {

    @Resource
    OperationLogService operationLogService;

    @Override
    public ResultHolder execute(IPMIUtil.Account account, BareMetal physicalMachine, OutBand outBand) {
        ResultHolder resultHolder = ResultHolder.error("");
        try {
            String commandResult = IPMIUtil.exeCommand(account, "power status");

            if (commandResult.contains(RackHDConstants.PM_POWER_ON) || commandResult.contains("On")) {
                IPMIUtil.exeCommand(account, "chassis bootdev pxe");
                IPMIUtil.exeCommand(account, "power reset");
                return ResultHolder.success("");
            } else if (commandResult.contains(RackHDConstants.PM_POWER_OFF) || commandResult.contains("Off")) {
                IPMIUtil.exeCommand(account, "chassis bootdev pxe");
                IPMIUtil.exeCommand(account, "power recycle");
                return ResultHolder.success("");
            }

            operationLogService.log(SessionUtil.getUser().getName(), "ipmi命令", "执行", account.getHost() + String.format("执行命令:chassis bootdev pxe 成功！"));
        } catch (Exception e) {
            return ResultHolder.error("ipmi命令调用失败！可能是网络不通或者账号密码错误或带外异常！");
        }
        return resultHolder;
    }
}
