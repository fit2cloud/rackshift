package io.rackshift.strategy.ipmihandler;

import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.mybatis.mapper.OutBandMapper;
import io.rackshift.service.OperationLogService;
import io.rackshift.strategy.ipmihandler.base.CommandHandler;
import io.rackshift.strategy.ipmihandler.base.IPMICommandHandlerAnnotation;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerInterface;
import io.rackshift.utils.IPMIUtil;
import io.rackshift.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

@IPMICommandHandlerAnnotation(operation = "pwd", desc = "改密")
public class PwdResetHandler extends CommandHandler implements IPMIHandlerInterface {
    @Resource
    OutBandMapper pmOutBandMapper;
    @Resource
    OperationLogService operationLogService;

    @Override
    public ResultHolder execute(IPMIUtil.Account account, BareMetal physicalMachine, OutBand outBand) {
        try {
            String brand = IPMIUtil.exeCommandForBrand(account);
            String userIndex = IPMIUtil.exeCommandForUserIndex(brand, account);
            int tryTimes = 0;
            do {
                if (userIndex.contains("Error")) {
                    userIndex = IPMIUtil.exeCommandForUserIndex(physicalMachine.getMachineBrand(), account);
                } else {
                    break;
                }
                tryTimes++;
            } while (tryTimes > 5);
            String commandResult = IPMIUtil.exeCommand(account, String.format("user set password %s %s", userIndex, account.getNewPwd()));

            if (commandResult.contains("successful") || StringUtils.isBlank(commandResult)) {
                operationLogService.log(SessionUtil.getUser().getName(), "ipmi命令", "执行", account.getHost() + String.format("执行命令:user set password %s %s 成功！", userIndex, account.getNewPwd()));
                outBand.setPwd(account.getNewPwd());
                pmOutBandMapper.updateByPrimaryKeySelective(outBand);
                return ResultHolder.success("修改密码成功！");
            } else {
                operationLogService.log(SessionUtil.getUser().getName(), "ipmi命令", "执行", account.getHost() + String.format("执行命令:user set password %s %s 失败！修改密码失败！合法的密码应该包含大小写字母数字特殊字符，总共不少于8个", userIndex, account.getNewPwd()));
                return ResultHolder.error("修改密码失败！合法的密码应该包含大小写字母数字特殊字符，总共不少于8个");
            }
        } catch (Exception e) {
            if (physicalMachine.getMachineBrand().toLowerCase().contains("dell") || physicalMachine.getMachineBrand().toLowerCase().contains("hp")) {
                outBand.setPwd(account.getNewPwd());
                pmOutBandMapper.updateByPrimaryKey(outBand);
                return ResultHolder.success("");
            }
            return ResultHolder.error("ipmi命令调用失败！可能是网络不通或者账号密码错误或带外异常！");
        }
    }
}
