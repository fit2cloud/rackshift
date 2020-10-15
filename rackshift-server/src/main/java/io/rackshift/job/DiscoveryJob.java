package io.rackshift.job;

import io.rackshift.constants.ServiceConstants;
import io.rackshift.job.model.DiscoveryTask;
import io.rackshift.mybatis.domain.BareMetalRule;
import io.rackshift.mybatis.domain.BareMetalRuleExample;
import io.rackshift.mybatis.mapper.BareMetalRuleMapper;
import io.rackshift.service.DiscoveryDevicesService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DiscoveryJob {
    private ExecutorService executors = Executors.newFixedThreadPool(10);
    @Resource
    private BareMetalRuleMapper bareMetalRuleMapper;
    @Resource
    private DiscoveryDevicesService discoveryDevicesService;
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(fixedDelay = 1000 * 60 * 120)
    public void run() {
        List<BareMetalRule> rules = bareMetalRuleMapper.selectByExample(new BareMetalRuleExample());
        if (rules.size() > 0) {
            CountDownLatch c = new CountDownLatch(rules.size());
            rules.forEach(r -> {
                if (ServiceConstants.DiscoveryStatusEnum.PENDING.name().equalsIgnoreCase(r.getSyncStatus())) {
                    executors.submit(new DiscoveryTask(r, bareMetalRuleMapper, discoveryDevicesService, simpMessagingTemplate, c));
                }
            });
            try {
                c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
