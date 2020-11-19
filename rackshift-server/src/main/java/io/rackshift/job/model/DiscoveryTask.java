package io.rackshift.job.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.PluginConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.metal.sdk.IMetalProvider;
import io.rackshift.metal.sdk.util.CloudProviderManager;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.model.MachineEntity;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.BareMetalRuleMapper;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.ExceptionUtils;
import io.rackshift.utils.IpUtil;
import io.rackshift.utils.LogUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 发现工作线程
 */
public class DiscoveryTask extends Thread {
    private BareMetalRule bareMetalRule;
    private BareMetalManager bareMetalManager;
    private SimpMessagingTemplate template;
    private BareMetalRuleMapper bareMetalRuleMapper;
    private CountDownLatch countDownLatch;
    private Cache cache;
    private CloudProviderManager cloudProviderManager;

    public DiscoveryTask(BareMetalRule bareMetalRule, BareMetalRuleMapper bareMetalRuleMapper, BareMetalManager bareMetalManager, SimpMessagingTemplate template, CloudProviderManager cloudProviderManager) {
        this.bareMetalRule = bareMetalRule;
        this.bareMetalManager = bareMetalManager;
        this.template = template;
        this.bareMetalRuleMapper = bareMetalRuleMapper;
        this.cloudProviderManager = cloudProviderManager;
    }

    public DiscoveryTask(BareMetalRule bareMetalRule, BareMetalRuleMapper bareMetalRuleMapper, BareMetalManager bareMetalManager, SimpMessagingTemplate template, CountDownLatch countDownLatch, Cache cache, CloudProviderManager cloudProviderManager) {
        this.bareMetalRule = bareMetalRule;
        this.bareMetalManager = bareMetalManager;
        this.template = template;
        this.bareMetalRuleMapper = bareMetalRuleMapper;
        this.countDownLatch = countDownLatch;
        this.cache = cache;
        this.cloudProviderManager = cloudProviderManager;
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
            String credentials = bareMetalRule.getCredentialParam();
            List<ProtocolRequest> paramList = new LinkedList<>();
            if (StringUtils.isNotBlank(credentials)) {
                paramList = fromCredentials(credentials);
            }
            if (ips != null && ips.size() > 0) {
                for (String ip : ips) {

                    Element brandsElement = null;
                    if (cache != null) {
                        brandsElement = cache.get(ip);
                    }
                    String brand = null;
                    if (brandsElement != null) {
                        brand = (String) brandsElement.getObjectValue();
                    } else {
                        brand = getMachineBrandThroughIp(ip);
                        brandsElement = new Element(ip, brand);
                        if (cache != null)
                            cache.put(brandsElement);
                    }

                    IMetalProvider iMetalProvider = null;
                    try {
                        iMetalProvider = cloudProviderManager.getCloudProvider(PluginConstants.PluginType.getPluginByBrand(brand));
                    } catch (Exception e) {
                        LogUtil.error(String.format("根据品牌获取插件出错!ip:%s", ip));
                        continue;
                    }
                    if (iMetalProvider == null) {
                        LogUtil.error("不支持的品牌,ip:" + ip);
                        continue;
                    }

                    for (ProtocolRequest request : paramList) {
                        MachineEntity entity = null;
                        if (ServiceConstants.SNMP.equalsIgnoreCase(request.getProtocol())) {
                            entity = convert(iMetalProvider.getMachineEntityThroughSNMP(JSONObject.toJSONString(request)));
                        } else {
                            entity = convert(iMetalProvider.getMachineEntity(JSONObject.toJSONString(request)));
                        }
                        if (entity != null) {
                            entity.setProviderId(iMetalProvider.getName());
                            entity.setRuleId(bareMetalRule.getId());
                            bareMetalManager.saveOrUpdateEntity(entity);
                        } else {
                            LogUtil.info("探测裸金属失败！" + JSONObject.toJSONString(request));
                        }
                    }
                }
            }
            bareMetalRule.setSyncStatus(ServiceConstants.DiscoveryStatusEnum.SUCCESS.name());

        } catch (Exception e) {
            bareMetalRule.setSyncStatus(ServiceConstants.DiscoveryStatusEnum.ERROR.name());
            LogUtil.error(String.format("嗅探发生异常:%s", ExceptionUtils.getExceptionDetail(e)));
        }
        template.convertAndSend("/topic/discovery", "");
        bareMetalRuleMapper.updateByPrimaryKey(bareMetalRule);

        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    private MachineEntity convert(io.rackshift.metal.sdk.model.MachineEntity machineEntity) {
        if (machineEntity == null) {
            return null;
        }
        MachineEntity r = new MachineEntity();
        BeanUtils.copyBean(r, machineEntity);
        r.setCpus(convertObject(machineEntity.getPmCpus(), Cpu.class));
        r.setMemories(convertObject(machineEntity.getPmMemories(), Memory.class));
        r.setDisks(convertObject(machineEntity.getDisks(), Disk.class));
        r.setNetworkCards(convertObject(machineEntity.getPmNetworkCards(), NetworkCard.class));
        return r;
    }

    private List convertObject(List objectList, Class cClass) {
        if (objectList == null)
            return null;
        List r = new LinkedList();
        objectList.forEach(o -> {
            try {
                Object obj = cClass.newInstance();
                BeanUtils.copyBean(obj, o);
                r.add(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return r;
    }

    private List<ProtocolRequest> fromCredentials(String credentials) {
        return JSONArray.parseArray(credentials).stream().map(c -> JSONObject.toJavaObject((JSONObject) c, ProtocolRequest.class)).collect(Collectors.toList());
    }

    public String getMachineBrandThroughIp(String ip) {
        //获取通用硬件信息
        try {
            String index = null;
            index = HttpFutureUtils.getHttps("https://" + ip, null);
            LogUtil.info(String.format("获取物理机%s品牌，接口返回数据%s", ip, index));
            if (StringUtils.isBlank(index)) {
                RSException.throwExceptions("该IP web管理界面无法打开！无法检测品牌！");
            }
            if (index.contains("IBM")) {
                return "IBM";
            } else if (index.contains("inspur")) {
                return "Inspur";
            } else if (index.contains("redirect()")) {
                return "DELL";
            } else if (index.contains("iLO")) {
                return "HP";
            }
        } catch (Exception e) {
            LogUtil.error(String.format("通过ip获取品牌时报错！ip:%s, e:%s", ip, ExceptionUtils.getExceptionDetail(e)));
            return "unknown";
        }
        return "unknown";
    }

}
