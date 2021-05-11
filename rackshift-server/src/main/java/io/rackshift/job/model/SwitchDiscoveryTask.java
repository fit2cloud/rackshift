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
import io.rackshift.mybatis.mapper.SwitchRuleMapper;
import io.rackshift.service.OutBandService;
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

    public SwitchDiscoveryTask(SwitchRule switchRule, SwitchRuleMapper bareMetalRuleMapper, BareMetalManager bareMetalManager, SimpMessagingTemplate template, CloudProviderManager cloudProviderManager, OutBandService outBandService) {
        this.switchRule = switchRule;
        this.bareMetalManager = bareMetalManager;
        this.template = template;
        this.bareMetalRuleMapper = bareMetalRuleMapper;
        this.cloudProviderManager = cloudProviderManager;
        this.outBandService = outBandService;
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

            Nmap4j nmap4j = new Nmap4j("/usr/local");
            nmap4j.includeHosts("192.168.1.1-255");
            nmap4j.excludeHosts("192.168.1.110");
            nmap4j.addFlags("-T3 -oX - -O -sV");
            nmap4j.execute();
            if (!nmap4j.hasError()) {
                NMapRun nmapRun = nmap4j.getResult();
            } else {
                System.out.println(nmap4j.getExecutionResults().getErrors());
            }
            String credentials = switchRule.getCredentialParam();
            List<ProtocolRequest> paramList = new LinkedList<>();
            if (StringUtils.isNotBlank(credentials)) {
                paramList = fromCredentials(credentials);
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

    private void onlyExtractIPMI(ProtocolRequest request, SwitchRule switchRule) throws Exception {
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
            physicalMachine.setRuleId(switchRule.getId());
            //插入ipmi发现的物理机之前 再次用序列号查询一次 没有重复的才能插入
            physicalMachine.setProviderId("");
            physicalMachine.setStatus(LifeStatus.onrack.name());
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

    public static void main(String[] args) throws NMapExecutionException, NMapInitializationException {

        Nmap4j nmap4j = new Nmap4j("C:\\Program Files (x86)\\Nmap");
        nmap4j.includeHosts("192.168.2.1");
        nmap4j.execute();
        if (!nmap4j.hasError()) {
            NMapRun nmapRun = nmap4j.getResult();
            System.out.println(JSONObject.toJSONString(nmapRun));
        } else {
            System.out.println(nmap4j.getExecutionResults().getErrors());
        }
    }
}
