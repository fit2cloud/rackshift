package io.rackshift.service;

import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.BareMetalDTO;
import io.rackshift.model.RSException;
import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.mybatis.domain.OutBandExample;
import io.rackshift.mybatis.mapper.OutBandMapper;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerDecorator;
import io.rackshift.utils.IPMIUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BareMetalService {
    @Resource
    private BareMetalManager bareMetalManager;
    @Resource
    private OutBandMapper outBandMapper;

    @Resource
    private IPMIHandlerDecorator ipmiHandlerDecorator;

    public List<BareMetalDTO> list(BareMetalDTO queryVO) {
        return bareMetalManager.list(queryVO);
    }

    public ResultHolder power(String id, String opt) {

        BareMetal pm = bareMetalManager.getBareMetalById(id);
//        if (pm == null || StringUtils.isBlank(pm.getServerId())) {
//            throw new RuntimeException("物理机不存在！或者RackHD nodeid没有设置！");
//        }
//
        if (pm == null) {
            throw new RuntimeException("物理机不存在！或者RackHD nodeid没有设置！");
        }

        /**1.获取对应物理的RackHD地址和验证信息 执行RESTfull调用
         * 2.RackHD的nodeId直接执行workflow
         **/
//        String discoveryId = pm.getRuleId();
//        BareMetalRule rule = physicalMachineRuleMapper.selectByPrimaryKey(discoveryId);
//        if (rule == null) {
//            throw new RuntimeException("物理机发现规则不存在！");
//        }
//        String url = JSONObject.parseObject(rule.getCredentialParam()).getString("rackHost");
//        if (url.endsWith("/")) {
//            url = url.substring(0, url.length() - 1);
//        }
        //改用IPMI直接开关机重启
        OutBandExample outbandExample = new OutBandExample();
        outbandExample.createCriteria().andIpEqualTo(pm.getManagementIp());

        List<OutBand> outBands = outBandMapper.selectByExample(outbandExample);
        if (outBands.size() < 1) {
            RSException.throwExceptions("请先配置带外信息！");
        }

        IPMIUtil.Account account = IPMIUtil.Account.build(outBands.get(0));
        ResultHolder resultHolder = null;
        if (!(resultHolder = ipmiHandlerDecorator.execute(opt, account, pm, outBands.get(0))).isSuccess()) {
            RSException.throwExceptions(resultHolder.getMessage());
        }
        return ResultHolder.success("");
    }
}
