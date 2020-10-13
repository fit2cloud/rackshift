package io.rackshift.service;

import io.rackshift.constants.ServiceConstants;
import io.rackshift.job.DiscoveryTask;
import io.rackshift.model.BareMetalRuleDTO;
import io.rackshift.mybatis.domain.BareMetalRule;
import io.rackshift.mybatis.domain.BareMetalRuleExample;
import io.rackshift.mybatis.mapper.BareMetalRuleMapper;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DiscoveryService {
    @Resource
    private BareMetalRuleMapper bareMetalRuleMapper;
    @Resource
    private DiscoveryDevicesService discoveryDevicesService;
    @Resource
    private EndpointMapper endpointMapper;
    @Autowired
    private SimpMessagingTemplate template;

    public Object add(BareMetalRuleDTO queryVO) {
        BareMetalRule bareMetalRule = new BareMetalRule();
        BeanUtils.copyBean(bareMetalRule, queryVO);
        bareMetalRule.setProviderId("");
        bareMetalRuleMapper.insertSelective(bareMetalRule);
        new Thread(new DiscoveryTask(bareMetalRule, bareMetalRuleMapper, discoveryDevicesService, template)).start();
        return true;
    }

    public Object update(BareMetalRuleDTO queryVO) {
        BareMetalRule BareMetalRule = new BareMetalRule();
        BeanUtils.copyBean(BareMetalRule, queryVO);
        BareMetalRule dbBareMetalRule = bareMetalRuleMapper.selectByPrimaryKey(queryVO.getId());
        bareMetalRuleMapper.updateByPrimaryKey(BareMetalRule);

        return true;
    }

    public Object del(String id) {
        BareMetalRule BareMetalRule = bareMetalRuleMapper.selectByPrimaryKey(id);
        if (BareMetalRule == null) return false;
        bareMetalRuleMapper.deleteByPrimaryKey(id);
        return false;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<BareMetalRule> list(BareMetalRuleDTO queryVO) {
        BareMetalRuleExample example = buildExample(queryVO);
        return bareMetalRuleMapper.selectByExample(example);
    }

    private BareMetalRuleExample buildExample(BareMetalRuleDTO queryVO) {
        return new BareMetalRuleExample();
    }

    public boolean sync(String[] ids) {
        for (String id : ids) {
            syncSingle(id);
        }
        return true;
    }

    private boolean syncSingle(String id) {
        if (bareMetalRuleMapper.selectByPrimaryKey(id) == null) {
            return false;
        }
        BareMetalRule rule = bareMetalRuleMapper.selectByPrimaryKey(id);
        if (ServiceConstants.DiscoveryStatusEnum.PENDING.name().equalsIgnoreCase(rule.getSyncStatus())) {
            return false;
        }
        rule.setSyncStatus(ServiceConstants.DiscoveryStatusEnum.PENDING.name());
        bareMetalRuleMapper.updateByPrimaryKey(rule);
        new Thread(new DiscoveryTask(rule, bareMetalRuleMapper, discoveryDevicesService, template)).start();
        return true;
    }
}
