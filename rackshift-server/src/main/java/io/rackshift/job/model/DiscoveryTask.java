package io.rackshift.job.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.config.PluginConfig;
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
import sun.rmi.runtime.Log;

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
    private PluginConfig pluginConfig;
    private OutBandService outBandService;

    public DiscoveryTask(BareMetalRule bareMetalRule, BareMetalRuleMapper bareMetalRuleMapper, BareMetalManager bareMetalManager, SimpMessagingTemplate template, PluginConfig pluginConfig, OutBandService outBandService) {
        this.bareMetalRule = bareMetalRule;
        this.bareMetalManager = bareMetalManager;
        this.template = template;
        this.bareMetalRuleMapper = bareMetalRuleMapper;
        this.pluginConfig = pluginConfig;
        this.outBandService = outBandService;
    }

    public DiscoveryTask(BareMetalRule bareMetalRule, BareMetalRuleMapper bareMetalRuleMapper, BareMetalManager bareMetalManager, SimpMessagingTemplate template, CountDownLatch countDownLatch, Cache cache, PluginConfig pluginConfig, OutBandService outBandService) {
        this.bareMetalRule = bareMetalRule;
        this.bareMetalManager = bareMetalManager;
        this.template = template;
        this.bareMetalRuleMapper = bareMetalRuleMapper;
        this.countDownLatch = countDownLatch;
        this.cache = cache;
        this.pluginConfig = pluginConfig;
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
                    if (!IpUtil.ping(ip))
                        continue;
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
                        iMetalProvider = pluginConfig.getPluginByBrand(brand);
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
                            LogUtil.error("爬虫抓取硬件信息失败！" + ExceptionUtils.getExceptionDetail(e));
                        }
                        if (entity != null) {
                            entity.setProviderId(iMetalProvider.getName());
                            entity.setRuleId(bareMetalRule.getId());
                            entity.setStatus(LifeStatus.onrack.name());
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

                                outBandService.saveOrUpdate(o, false);
                            }
                        } else {
                            ProtocolRequest r = new ProtocolRequest();
                            BeanUtils.copyBean(r, request);
                            r.setPwd("******");
                            LogUtil.error("使用插件探测裸金属失败！" + JSONObject.toJSONString(r));
                            if (ServiceConstants.IPMI_Rest.equalsIgnoreCase(request.getProtocol())) {
                                onlyExtractIPMI(request, bareMetalRule);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error(String.format("嗅探发生异常:%s", ExceptionUtils.getExceptionDetail(e)));
        }
        bareMetalRule.setSyncStatus(ServiceConstants.DiscoveryStatusEnum.COMPLETE.name());
        template.convertAndSend("/topic/discovery", "");
        bareMetalRuleMapper.updateByPrimaryKey(bareMetalRule);

        if (countDownLatch != null) {
            countDownLatch.countDown();
        }

    }

    private void onlyExtractIPMI(ProtocolRequest request, BareMetalRule bareMetalRule) throws Exception {
        try {
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
            if (StringUtils.isNotBlank(name)) {
                physicalMachine.setMachineModel(name);
            }
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
            if (StringUtils.isBlank(physicalMachine.getManagementIp())) {
                physicalMachine.setManagementIp("unknown ip");
                if (StringUtils.isNotBlank(physicalMachine.getMachineSn())) {
                    LogUtil.warn(String.format("带外 ip 不存在的序列号:%s", physicalMachine.getMachineSn()));
                } else {
                    LogUtil.warn(String.format("带外 ip 序列号都不存在的机器:%s", JSONObject.toJSONString(physicalMachine)));
                }
            }
            boolean r = bareMetalManager.addToBareMetal(physicalMachine);
            saveOutBand(account, physicalMachine, r);
        } catch (Exception e) {
            LogUtil.error("ipmi爬取信息失败", ExceptionUtils.getExceptionDetail(e));
        }
    }

    private void saveOutBand(IPMIUtil.Account account, BareMetal bareMetal, boolean r) {
        if (!r) return;
        OutBand o = new OutBand();
        o.setIp(account.getHost());
        o.setBareMetalId(bareMetal.getId());
        o.setUserName(account.getUserName());
        o.setPwd(account.getPwd());
        outBandService.saveOrUpdate(o, false);
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


    public static void main(String[] args) {

//        try {
//            ProtocolRequest request = new ProtocolRequest();
//            IPMIUtil.Account account = IPMIUtil.Account.build(request.getHost(), request.getUserName(), request.getPwd());
//            //获取通用硬件信息
//            JSONObject fruObj = JSONObject.parseObject("{\"Chassis Type\":\"Rack Mount Chassis\",\"Board Product\":\"RS33M2C9S\",\"Board Mfg Date\":\"Wed Jan 27 16:00:00 2021\",\"Board Extra\":\"210235A2CR6212F00041\",\"Product Name\":\"UniServer R4900 G3\",\"Product Serial\":\"210235A2CR6212F00041\",\"FRU Device Description\":\"Builtin FRU Device (ID 0)\",\"Board Mfg\":\"H3C\",\"Board Serial\":\"02A3FQ6211F0070Z\",\"Product Part Number\":\"0235A2CR\",\"Product Version\":\"2.00.47\",\"Chassis Part Number\":\"0235A2CR\",\"Chassis Extra\":\"210200A00Q6212F00075\",\"Chassis Serial\":\"210235A2CR6212F00041\",\"Board Part Number\":\"0302A3FQ\",\"Product Manufacturer\":\"New H3C Technologies Co., Ltd.\"}");
//            //获取bmc网卡信息
//            JSONObject lanObj = JSONObject.parseObject("{\"SNMP Community String\":\"AMI\",\"BMC ARP Control\":\"ARP Responses Enabled, Gratuitous ARP Disabled\",\"RMCP+ Cipher Suites\":\"0,1,2,3,6,7,8,11,12,15,16,17\",\"IP Header\":\"TTL=0x40 Flags=0x40 Precedence=0x00 TOS=0x10\",\"802.1q VLAN ID\":\"Disabled\",\"Auth Type Support\":\"MD5\",\"IP Address Source\":\"DHCP Address\",\"Gratituous ARP Intrvl\":\"0.0 seconds\",\"Default Gateway IP\":\"43.14.101.254\",\"802.1q VLAN Priority\":\"0\",\"User Lockout Interval\":\"0\",\"MAC Address\":\"84:65:69:5c:a2:a4\",\"Backup Gateway MAC\":\"00:00:00:00:00:00\",\"Invalid password disable\":\"no\",\"IP Address\":\"43.14.101.5\",\"Subnet Mask\":\"255.255.255.0\",\"Backup Gateway IP\":\"0.0.0.0\",\"Attempt Count Reset Int.\":\"0\",\"Auth Type Enable\":\"Callback : MD5\",\"Set in Progress\":\"Set Complete\",\"Default Gateway MAC\":\"3c:d2:e5:36:ca:01\",\"Bad Password Threshold\":\"0\",\"Cipher Suite Priv Max\":\"caaaaaaaaaaaXXX\"}");
//
//            String powerResult = "power is on";
//
//            String machineBrand = fruObj.getString("Product Manufacturer");
//            String machineSn = fruObj.getString("Product Serial");
//            String name = machineBrand + " " + fruObj.getString("Product Name");
//
//            BareMetal physicalMachine = new BareMetal();
//            if (StringUtils.isNotBlank(name)) {
//                physicalMachine.setMachineModel(name);
//            }
//            physicalMachine.setId(UUIDUtil.newUUID());
//            if (powerResult.contains(RackHDConstants.PM_POWER_ON)) {
//                physicalMachine.setPower(RackHDConstants.PM_POWER_ON);
//            } else if (powerResult.contains(RackHDConstants.PM_POWER_OFF)) {
//                physicalMachine.setPower(RackHDConstants.PM_POWER_OFF);
//            } else {
//                physicalMachine.setPower(RackHDConstants.PM_POWER_UNKNOWN);
//            }
//            physicalMachine.setManagementIp(account.getHost());
//            physicalMachine.setMachineSn(machineSn);
//            if (PluginConstants.DELL.equalsIgnoreCase(machineBrand)) {
//                physicalMachine.setMachineModel(PluginConstants.DELL + " " + fruObj.getString("Board Product"));
//            }
//            physicalMachine.setMachineBrand(machineBrand);
//            physicalMachine.setBmcMac(lanObj.getString("MAC Address"));
//            //插入ipmi发现的物理机之前 再次用序列号查询一次 没有重复的才能插入
//            physicalMachine.setProviderId("");
//            physicalMachine.setStatus(LifeStatus.onrack.name());
//            boolean r = bareMetalManager.addToBareMetal(physicalMachine);
//            saveOutBand(account, physicalMachine, r);
//        } catch (Exception e) {
//            LogUtil.error("ipmi爬取信息失败", ExceptionUtils.getExceptionDetail(e));
//        }

    }
}
