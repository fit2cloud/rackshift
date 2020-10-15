package io.rackshift.job.model;

import io.rackshift.constants.ServiceConstants;
import io.rackshift.mybatis.domain.BareMetalRule;
import io.rackshift.mybatis.domain.DiscoveryDevices;
import io.rackshift.mybatis.mapper.BareMetalRuleMapper;
import io.rackshift.service.DiscoveryDevicesService;
import io.rackshift.utils.ExceptionUtils;
import io.rackshift.utils.IpUtil;
import io.rackshift.utils.LogUtil;
import io.rackshift.utils.UUIDUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 发现工作线程
 */
public class DiscoveryTask extends Thread {
    private BareMetalRule bareMetalRule;
    private DiscoveryDevicesService discoveryDevicesService;
    private SimpMessagingTemplate template;
    private BareMetalRuleMapper bareMetalRuleMapper;
    private CountDownLatch countDownLatch;

    public DiscoveryTask(BareMetalRule bareMetalRule, BareMetalRuleMapper bareMetalRuleMapper, DiscoveryDevicesService discoveryDevicesService, SimpMessagingTemplate template) {
        this.bareMetalRule = bareMetalRule;
        this.discoveryDevicesService = discoveryDevicesService;
        this.template = template;
        this.bareMetalRuleMapper = bareMetalRuleMapper;
    }

    public DiscoveryTask(BareMetalRule bareMetalRule, BareMetalRuleMapper bareMetalRuleMapper, DiscoveryDevicesService discoveryDevicesService, SimpMessagingTemplate template, CountDownLatch countDownLatch) {
        this.bareMetalRule = bareMetalRule;
        this.discoveryDevicesService = discoveryDevicesService;
        this.template = template;
        this.bareMetalRuleMapper = bareMetalRuleMapper;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            //从定时任务进来先上锁
            if (countDownLatch != null) {
                //如果并发的进入则只能有一个线程进行处理
                if (ServiceConstants.DiscoveryStatusEnum.PENDING.name().equalsIgnoreCase(bareMetalRule.getSyncStatus())) {
                    countDownLatch.countDown();
                    return;
                }
                bareMetalRule.setSyncStatus(ServiceConstants.DiscoveryStatusEnum.PENDING.name());
                bareMetalRuleMapper.updateByPrimaryKey(bareMetalRule);
            }
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
            LogUtil.error(String.format("嗅探ping发生异常:%s", ExceptionUtils.getExceptionDetail(e)));
        }
        template.convertAndSend("/topic/discovery", "");
        bareMetalRuleMapper.updateByPrimaryKey(bareMetalRule);

        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }
}
