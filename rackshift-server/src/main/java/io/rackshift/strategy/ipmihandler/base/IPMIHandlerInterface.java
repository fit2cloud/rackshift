package io.rackshift.strategy.ipmihandler.base;


import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.utils.IPMIUtil;

public interface IPMIHandlerInterface {
    /**
     * 处理一个命令
     *
     * @param account         账户
     * @param physicalMachine 物理机
     * @param outBand         带外
     * @return
     */
    ResultHolder execute(IPMIUtil.Account account, BareMetal physicalMachine, OutBand outBand);
}
