package io.rackshift.job.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.PluginConstants;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.metal.sdk.IMetalProvider;
import io.rackshift.metal.sdk.util.CloudProviderManager;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.metal.sdk.util.SnmpWorker;
import io.rackshift.model.MachineEntity;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.SwitchRuleMapper;
import io.rackshift.service.OutBandService;
import io.rackshift.service.SwitchService;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.*;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.nmap4j.Nmap4j;
import org.nmap4j.core.nmap.NMapExecutionException;
import org.nmap4j.core.nmap.NMapInitializationException;
import org.nmap4j.data.NMapRun;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 发现工作线程
 */
public class SwitchDiscoveryTask extends Thread {
    private SwitchRule switchRule;
    private BareMetalManager bareMetalManager;
    private SimpMessagingTemplate template;
    private SwitchRuleMapper bareMetalRuleMapper;
    private CountDownLatch countDownLatch;
    private Cache cache;
    private CloudProviderManager cloudProviderManager;
    private OutBandService outBandService;
    private SwitchService switchService;

    public SwitchDiscoveryTask(SwitchRule switchRule, SwitchRuleMapper bareMetalRuleMapper, BareMetalManager bareMetalManager, SimpMessagingTemplate template, CloudProviderManager cloudProviderManager, OutBandService outBandService, SwitchService switchService) {
        this.switchRule = switchRule;
        this.bareMetalManager = bareMetalManager;
        this.template = template;
        this.bareMetalRuleMapper = bareMetalRuleMapper;
        this.cloudProviderManager = cloudProviderManager;
        this.outBandService = outBandService;
        this.switchService = switchService;
    }

    public SwitchDiscoveryTask(SwitchRule switchRule, SwitchRuleMapper bareMetalRuleMapper, BareMetalManager bareMetalManager, SimpMessagingTemplate template, CountDownLatch countDownLatch, Cache cache, CloudProviderManager cloudProviderManager, OutBandService outBandService) {
        this.switchRule = switchRule;
        this.bareMetalManager = bareMetalManager;
        this.template = template;
        this.bareMetalRuleMapper = bareMetalRuleMapper;
        this.countDownLatch = countDownLatch;
        this.cache = cache;
        this.cloudProviderManager = cloudProviderManager;
        this.outBandService = outBandService;
    }

    @Override
    public void run() {
        try {
            //从定时任务进来先上锁
            if (countDownLatch != null) {
                //如果并发的进入则只能有一个线程进行处理
                if (ServiceConstants.DiscoveryStatusEnum.PENDING.name().equalsIgnoreCase(switchRule.getSyncStatus())) {
                    countDownLatch.countDown();
                    return;
                }
                switchRule.setSyncStatus(ServiceConstants.DiscoveryStatusEnum.PENDING.name());
                bareMetalRuleMapper.updateByPrimaryKey(switchRule);
            }

            String credentials = switchRule.getCredentialParam();
            List<ProtocolRequest> paramList = new LinkedList<>();
            if (StringUtils.isNotBlank(credentials)) {
                paramList = fromCredentials(credentials);
            }
            List<String> ips = IpUtil.getIpRange(switchRule.getStartIp(), switchRule.getEndIp(), switchRule.getMask());
            if (ips != null && ips.size() > 0) {
                for (String ip : ips) {
                    for (ProtocolRequest request : paramList) {
                        if (ServiceConstants.SNMP.equalsIgnoreCase(request.getProtocol())) {
                            if (StringUtils.isNotBlank(request.getCommunity())) {
                                SnmpWorker snmpWorker = new SnmpWorker(ip, request.getCommunity(), request.getPort());
                                request.setHost(ip);
                                Switch switchDevice = switchService.getMachineEntity(request, switchRule, snmpWorker);
                                switchService.saveOrUpdate(switchDevice);
                                //
                                queryPortAndBondMacAddress(snmpWorker, switchDevice);
                            }
                        } else {
                            //暂时不支持其他协议的发现
                        }
                    }
                }
            }

        } catch (Exception e) {
            LogUtil.error(String.format("嗅探发生异常:%s", ExceptionUtils.getExceptionDetail(e)));
        }
        switchRule.setSyncStatus(ServiceConstants.DiscoveryStatusEnum.COMPLETE.name());
        template.convertAndSend("/topic/discovery", "");
        bareMetalRuleMapper.updateByPrimaryKey(switchRule);

        if (countDownLatch != null) {
            countDownLatch.countDown();
        }

    }

    private void queryPortAndBondMacAddress(SnmpWorker snmpWorker, Switch switchDevice) {
        Map<String, String> portMap = snmpWorker.walk("1.3.6.1.2.1.2.2.1.2");
        Map<String, String> macMap = snmpWorker.walk("1.3.6.1.2.1.17.4.3.1.1");
        Map<String, String> portNumMap = snmpWorker.walk("1.3.6.1.2.1.17.4.3.1.2");

        List<SwitchPort> ports = new LinkedList();
        if (portMap != null) {
            macMap.keySet().forEach(p -> {
                SwitchPort switchPort = new SwitchPort();
                switchPort.setPort(portNumMap.get(p.replace("1.3.6.1.2.1.17.4.3.1.1", "1.3.6.1.2.1.17.4.3.1.2")));
                switchPort.setMac(macMap.get(p));
                switchPort.setSwitchId(switchDevice.getId());
                ports.add(switchPort);
            });
        }

        switchService.saveOrUpdatePorts(ports);
    }

    private List<ProtocolRequest> fromCredentials(String credentials) {
        return JSONArray.parseArray(credentials).stream().map(c -> JSONObject.toJavaObject((JSONObject) c, ProtocolRequest.class)).collect(Collectors.toList());
    }
}
