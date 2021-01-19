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
import io.rackshift.model.MachineEntity;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.BareMetalRuleMapper;
import io.rackshift.service.OutBandService;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.*;
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
    private OutBandService outBandService;

    public DiscoveryTask(BareMetalRule bareMetalRule, BareMetalRuleMapper bareMetalRuleMapper, BareMetalManager bareMetalManager, SimpMessagingTemplate template, CloudProviderManager cloudProviderManager, OutBandService outBandService) {
        this.bareMetalRule = bareMetalRule;
        this.bareMetalManager = bareMetalManager;
        this.template = template;
        this.bareMetalRuleMapper = bareMetalRuleMapper;
        this.cloudProviderManager = cloudProviderManager;
        this.outBandService = outBandService;
    }

    public DiscoveryTask(BareMetalRule bareMetalRule, BareMetalRuleMapper bareMetalRuleMapper, BareMetalManager bareMetalManager, SimpMessagingTemplate template, CountDownLatch countDownLatch, Cache cache, CloudProviderManager cloudProviderManager, OutBandService outBandService) {
        this.bareMetalRule = bareMetalRule;
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
                    }
                    if (iMetalProvider == null) {
                        LogUtil.error("不支持的品牌,ip:" + ip);
                    }

                    for (ProtocolRequest request : paramList) {
                        MachineEntity entity = null;
                        //使用不同的协议去测试爬取硬件信息
                        request.setHost(ip);
                        try {
                            if (ServiceConstants.SNMP.equalsIgnoreCase(request.getProtocol())) {
                                entity = convert(iMetalProvider.getMachineEntityThroughSNMP(JSONObject.toJSONString(request)));
                            } else {
                                entity = convert(iMetalProvider.getMachineEntity(JSONObject.toJSONString(request)));
                            }
                        } catch (Exception e) {
                            LogUtil.info("爬虫抓取硬件信息失败！" + ExceptionUtils.getExceptionDetail(e));
                        }
                        if (entity != null) {
                            entity.setProviderId(iMetalProvider.getName());
                            entity.setRuleId(bareMetalRule.getId());
                            BareMetal bareMetal = bareMetalManager.saveOrUpdateEntity(entity);
                            if (bareMetal == null) {
                                LogUtil.error("保存machineEntity失敗！" + ip);
                                continue;
                            }
                            if (ServiceConstants.IPMI_Rest.equalsIgnoreCase(request.getProtocol())) {
                                OutBand o = new OutBand();
                                o.setBareMetalId(bareMetal.getId());
                                o.setIp(ip);
                                o.setUserName(request.getUserName());
                                o.setPwd(request.getPwd());

                                outBandService.saveOrUpdate(o);
                            }
                        } else {
                            LogUtil.info("使用插件探测裸金属失败！" + JSONObject.toJSONString(request));
                            if (ServiceConstants.IPMI_Rest.equalsIgnoreCase(request.getProtocol())) {
                                onlyExtractIPMI(request, bareMetalRule);
                            }
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

    private void onlyExtractIPMI(ProtocolRequest request, BareMetalRule bareMetalRule) throws Exception {
        IPMIUtil.Account account = IPMIUtil.Account.build(request.getHost(), request.getUserName(), request.getPwd());
        //获取通用硬件信息
        String commandResult = IPMIUtil.exeCommand(account, "fru");
        JSONObject fruObj = IPMIUtil.transform(commandResult);
        //获取bmc网卡信息
        String bmcResult = IPMIUtil.exeCommand(account, "lan print");
        JSONObject lanObj = IPMIUtil.transform(bmcResult);

        String powerResult = IPMIUtil.exeCommand(account, "power status");

        String machineBrand = fruObj.getString("Product Manufacturer");
        String machineSn = fruObj.getString("Product Serial");
        String name = machineBrand + " " + fruObj.getString("Product Name");

        BareMetal physicalMachine = new BareMetal();
        physicalMachine.setMachineModel(name);
        physicalMachine.setId(UUIDUtil.newUUID());
        if (powerResult.contains(RackHDConstants.PM_POWER_ON)) {
            physicalMachine.setPower(RackHDConstants.PM_POWER_ON);
        } else if (powerResult.contains(RackHDConstants.PM_POWER_OFF)) {
            physicalMachine.setPower(RackHDConstants.PM_POWER_OFF);
        } else {
            physicalMachine.setPower(RackHDConstants.PM_POWER_UNKNOWN);
        }
        physicalMachine.setManagementIp(account.getHost());
        physicalMachine.setMachineSn(machineSn);
        if (PluginConstants.DELL.equalsIgnoreCase(machineBrand)) {
            physicalMachine.setMachineModel(PluginConstants.DELL + " " + fruObj.getString("Board Product"));
        }
        physicalMachine.setMachineBrand(machineBrand);
        physicalMachine.setBmcMac(lanObj.getString("MAC Address"));
        physicalMachine.setRuleId(bareMetalRule.getId());
        //插入ipmi发现的物理机之前 再次用序列号查询一次 没有重复的才能插入
        physicalMachine.setProviderId("");
        physicalMachine.setStatus(LifeStatus.onrack.name());
        boolean r = bareMetalManager.addToBareMetal(physicalMachine);
        saveOutBand(account, physicalMachine, r);
    }

    private void saveOutBand(IPMIUtil.Account account, BareMetal bareMetal, boolean r) {
        if (!r) return;
        OutBand o = new OutBand();
        o.setIp(account.getHost());
        o.setBareMetalId(bareMetal.getId());
        o.setUserName(account.getUserName());
        o.setPwd(account.getPwd());
        outBandService.saveOrUpdate(o);
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
