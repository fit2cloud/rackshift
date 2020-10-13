package io.rackshift.job;

import io.rackshift.constants.ServiceConstants;
import io.rackshift.mybatis.domain.BareMetalRule;
import io.rackshift.mybatis.domain.DiscoveryDevices;
import io.rackshift.mybatis.mapper.BareMetalRuleMapper;
import io.rackshift.service.DiscoveryDevicesService;
import io.rackshift.utils.IpUtil;
import io.rackshift.utils.UUIDUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

/**
 * 发现工作线程
 */
public class DiscoveryTask extends Thread {
    private BareMetalRule bareMetalRule;
    private DiscoveryDevicesService discoveryDevicesService;
    private SimpMessagingTemplate template;
    private BareMetalRuleMapper bareMetalRuleMapper;

    public DiscoveryTask(BareMetalRule bareMetalRule, BareMetalRuleMapper bareMetalRuleMapper, DiscoveryDevicesService discoveryDevicesService, SimpMessagingTemplate template) {
        this.bareMetalRule = bareMetalRule;
        this.discoveryDevicesService = discoveryDevicesService;
        this.template = template;
        this.bareMetalRuleMapper = bareMetalRuleMapper;
    }

    @Override
    public void run() {
        try {
            List<String> ips = IpUtil.getIpRange(bareMetalRule.getStartIp(), bareMetalRule.getEndIp(), bareMetalRule.getMask());
            if (ips != null && ips.size() > 0) {
                ips.forEach(ip -> {
                    if (io.rackshift.metal.sdk.util.IpUtil.ping(ip)) {
                        DiscoveryDevices d = discoveryDevicesService.getByIp(ip);
                        if (d != null) {
                            discoveryDevicesService.update(d);
                        } else {
                            d = new DiscoveryDevices();
                            d.setDescription("ping");
                            d.setIp(ip);
                            d.setName(UUIDUtil.newUUID());
                            d.setBareMetalRuleId(bareMetalRule.getId());
                            discoveryDevicesService.add(d);
                        }
                    }
                });
            }
            bareMetalRule.setSyncStatus(ServiceConstants.DiscoveryStatusEnum.SUCCESS.name());

        } catch (Exception e) {
            bareMetalRule.setSyncStatus(ServiceConstants.DiscoveryStatusEnum.ERROR.name());
            //::todo
        }
        template.convertAndSend("/topic/discovery", "");
        bareMetalRuleMapper.updateByPrimaryKey(bareMetalRule);
    }
}
