package io.rackshift.service;

import io.rackshift.constants.ServiceConstants;
import io.rackshift.job.model.DiscoveryTask;
import io.rackshift.job.model.SwitchDiscoveryTask;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.metal.sdk.util.CloudProviderManager;
import io.rackshift.model.SwitchRuleDTO;
import io.rackshift.model.BareMetalRuleVO;
import io.rackshift.model.SwitchRuleDTO;
import io.rackshift.mybatis.domain.SwitchRule;
import io.rackshift.mybatis.domain.BareMetalRuleExample;
import io.rackshift.mybatis.mapper.BareMetalRuleMapper;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.mybatis.mapper.SwitchRuleMapper;
import io.rackshift.mybatis.mapper.ext.ExtBareMetalRuleMapper;
import io.rackshift.mybatis.mapper.ext.ExtSwitchRuleMapper;
import io.rackshift.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SwitchDiscoveryService {
    @Resource
    private SwitchRuleMapper switchRuleMapper;
    @Resource
    private ExtSwitchRuleMapper extSwitchRuleMapper;
    @Resource
    private BareMetalManager bareMetalManager;
    @Autowired
    private SimpMessagingTemplate template;
    @Resource
    private CloudProviderManager metalProviderManager;
    @Resource
    private OutBandService outBandService;

    public Object add(SwitchRuleDTO queryVO) {
        SwitchRule switchRule = new SwitchRule();
        BeanUtils.copyBean(switchRule, queryVO);
        switchRule.setProviderId("");
        switchRule.setLastSyncTimestamp(System.currentTimeMillis());
        switchRuleMapper.insertSelective(switchRule);
        new Thread(new SwitchDiscoveryTask(switchRule, switchRuleMapper, bareMetalManager, template, metalProviderManager, outBandService)).start();
        return true;
    }

    public Object update(SwitchRuleDTO queryVO) {
        SwitchRule SwitchRule = new SwitchRule();
        BeanUtils.copyBean(SwitchRule, queryVO);
        switchRuleMapper.updateByPrimaryKeyWithBLOBs(SwitchRule);

        return true;
    }

    public Object del(String id) {
        SwitchRule SwitchRule = switchRuleMapper.selectByPrimaryKey(id);
        if (SwitchRule == null) return false;
        switchRuleMapper.deleteByPrimaryKey(id);
        return false;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<SwitchRuleDTO> list(SwitchRuleDTO queryVO) {
        return extSwitchRuleMapper.list(queryVO);
    }

    private BareMetalRuleExample buildExample(SwitchRuleDTO queryVO) {
        return new BareMetalRuleExample();
    }

    public boolean sync(String[] ids) {
        for (String id : ids) {
            syncSingle(id);
        }
        return true;
    }

    private boolean syncSingle(String id) {
        if (switchRuleMapper.selectByPrimaryKey(id) == null) {
            return false;
        }
        SwitchRule rule = switchRuleMapper.selectByPrimaryKey(id);
//        if (ServiceConstants.DiscoveryStatusEnum.PENDING.name().equalsIgnoreCase(rule.getSyncStatus())) {
//            return false;
//        }
        rule.setSyncStatus(ServiceConstants.DiscoveryStatusEnum.PENDING.name());
        switchRuleMapper.updateByPrimaryKey(rule);
        new Thread(new SwitchDiscoveryTask(rule, switchRuleMapper, bareMetalManager, template, metalProviderManager, outBandService)).start();
        return true;
    }
}
