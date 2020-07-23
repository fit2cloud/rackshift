package io.rackshift.strategy.ipmihandler.base;

import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.utils.IPMIUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IPMIHandlerDecorator {

    private Map<String, IPMIHandlerInterface> handlerMap = new HashMap<>();

    public ResultHolder execute(String operation, IPMIUtil.Account account, BareMetal physicalMachine, OutBand outBand) {
        IPMIHandlerInterface handler = handlerMap.get(operation);

        ResultHolder x = check(account, handler);
        if (x != null) return x;

        return handler.execute(account, physicalMachine, outBand);

    }

    private ResultHolder check(IPMIUtil.Account account, IPMIHandlerInterface handler) {
        if (handler == null) {
            return ResultHolder.error("命令执行失败！该操作不支持！");
        }
        if (StringUtils.isAnyBlank(account.getHost(), account.getUserName(), account.getPwd())) {
            return ResultHolder.error("命令执行失败！参数不合法！");
        }
        return null;
    }

    public ResultHolder handleCommand(List<String> operations, IPMIUtil.Account account, BareMetal physicalMachine, OutBand outBand) {
        ResultHolder result = null;
        for (String operation : operations) {
            IPMIHandlerInterface handler = handlerMap.get(operation);

            ResultHolder x = check(account, handler);
            if (x != null) return x;

            result = handler.execute(account, physicalMachine, outBand);
            if (!result.isSuccess()) {
                return result;
            }
        }
        return result;
    }

    public Map<String, IPMIHandlerInterface> getHandlerMap() {
        return handlerMap;
    }
}
