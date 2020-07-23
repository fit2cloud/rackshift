package io.rackshift.strategy.ipmihandler;

import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.strategy.ipmihandler.base.CommandHandler;
import io.rackshift.strategy.ipmihandler.base.IPMICommandHandlerAnnotation;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerInterface;
import io.rackshift.utils.IPMIUtil;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

@IPMICommandHandlerAnnotation(operation = "ip", desc = "修改ip地址")
public class IPHandler extends CommandHandler implements IPMIHandlerInterface {
    @Resource
    CommandHandler commandHandler;

    @Override
    public ResultHolder execute(IPMIUtil.Account account, BareMetal physicalMachine, OutBand outBand) {
        ResultHolder resultHolder = null;
        try {
            if (StringUtils.isNotBlank(account.getNewIp())) {
                String result1 = null;
                if (!physicalMachine.getMachineBrand().equalsIgnoreCase("hp")) {
                    try {
                        result1 = IPMIUtil.exeCommand(account, "lan set 1 ipsrc static");
                    } catch (Exception e) {
                        if (StringUtils.isBlank(result1) || result1.contains("failed")) {
                            String result2 = IPMIUtil.exeCommand(account, String.format("lan set 1 ipaddr %s", account.getNewIp()));
                            if (result2.contains("failed") || result2.contains("Setting")) {
                                return ResultHolder.success("");
                            }
                        }
                    }
                } else {
                    try {
                        result1 = IPMIUtil.exeCommand(account, "lan set 2 ipsrc static");
                    } catch (Exception e) {
                        if (StringUtils.isBlank(result1) || result1.contains("failed")) {
                            String result2 = IPMIUtil.exeCommand(account, String.format("lan set 2 ipaddr %s", account.getNewIp()));
                            if (result2.contains("failed") || result2.contains("Setting")) {
                                IPMIUtil.exeCommand(account, "mc reset cold");
                                return ResultHolder.success("");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return ResultHolder.error("ipmi命令调用失败！可能是网络不通或者账号密码错误或带外异常！");
        }
        if (resultHolder.isSuccess()) {
            return ResultHolder.success("ipmi修改ip命令调用成功！");
        }
        return ResultHolder.success("ipmi修改ip命令调用失败！");
    }
}
