package io.rackshift.strategy.ipmihandler.base;

import com.alibaba.fastjson.JSON;
import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.service.OperationLogService;
import io.rackshift.utils.IPMIUtil;
import io.rackshift.utils.SessionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CommandHandler {
    @Resource
    OperationLogService operationLogService;

    public ResultHolder execute(IPMIUtil.Account account, BareMetal physicalMachine, OutBand outBand, String commands) {
        try {
            IPMIUtil.exeCommand(account, commands);
            operationLogService.log(SessionUtil.getUser().getName(), "ipmi命令", "执行", account.getHost() + "执行命令:" + commands + " 成功！");
            return ResultHolder.success("");
        } catch (Exception e) {
            operationLogService.log(SessionUtil.getUser().getName(), "ipmi命令", "执行", account.getHost() + "执行命令:" + commands + " 失败！");
            return ResultHolder.error("ipmi命令调用失败！可能是网络不通或者账号密码错误或带外异常！account:[" + JSON.toJSONString(account) + "] e:" + e);
        }
    }
}
