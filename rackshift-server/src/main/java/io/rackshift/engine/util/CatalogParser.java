package io.rackshift.engine.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import io.rackshift.constants.PluginConstants;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.metal.sdk.util.DiskUtils;
import io.rackshift.model.MachineEntity;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.BareMetalMapper;
import io.rackshift.mybatis.mapper.CatalogMapper;
import io.rackshift.service.OutBandService;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.DiskSizeUtils;
import io.rackshift.utils.IPMIUtil;
import io.rackshift.utils.LogUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatalogParser {
    @Resource
    private OutBandService outBandService;
    @Resource
    private CatalogMapper catalogMapper;

    private List<Disk> getLatestDisk(List<Disk> disks) {
        if (CollectionUtils.isEmpty(disks)) {
            return disks;
        }
        Map<Long, List<Disk>> diskMap = disks.stream().collect(Collectors.groupingBy(Disk::getSyncTime));
        return diskMap.get(diskMap.keySet().stream().max(Long::compareTo).orElse(null));
    }

    private List<NetworkCard> getLatestCards(List<NetworkCard> pmNetworkCards) {
        if (CollectionUtils.isEmpty(pmNetworkCards)) {
            return pmNetworkCards;
        }
        Map<Long, List<NetworkCard>> diskMap = pmNetworkCards.stream().collect(Collectors.groupingBy(NetworkCard::getSyncTime));
        return diskMap.get(diskMap.keySet().stream().max(Long::compareTo).orElse(null));
    }

    public BareMetal convertToPm(MachineEntity machineEntity) throws Exception {
        BareMetal physicalMachine = new BareMetal();
        BeanUtils.copyProperties(physicalMachine, machineEntity);

        physicalMachine.setId(machineEntity.getNodeId());
        physicalMachine.setMachineModel(machineEntity.getBrand() + " " + machineEntity.getModel());
        physicalMachine.setUpdateTime(System.currentTimeMillis());
        physicalMachine.setStatus(Optional.ofNullable(machineEntity.getStatus()).orElse(LifeStatus.ready.toString()));
        physicalMachine.setManagementIp(machineEntity.getBmcIp());
        physicalMachine.setMachineSn(machineEntity.getSerialNo());

        physicalMachine.setEndpointId(machineEntity.getEndPoint());
        physicalMachine.setProviderId(Optional.ofNullable(machineEntity.getProviderId()).orElse("rackhd"));
        physicalMachine.setRuleId(machineEntity.getRuleId());
        physicalMachine.setMachineBrand(machineEntity.getBrand());
        physicalMachine.setCreateTime(System.currentTimeMillis());
        physicalMachine.setMemory(physicalMachine.getMemory());
        physicalMachine.setDisk(physicalMachine.getDisk());
        physicalMachine.setMemoryType(machineEntity.getMemoryType());
        int diskSize = 0;
        String thisDisk;
        if (!org.apache.shiro.util.CollectionUtils.isEmpty(machineEntity.getDisks())) {
            for (Disk d : machineEntity.getDisks()) {
                thisDisk = d.getSize();
                diskSize += Integer.parseInt(thisDisk.replace(thisDisk.replaceAll("\\d+", ""), ""));
            }
        }

        physicalMachine.setDisk(diskSize);
        if (diskSize == 0)
            physicalMachine.setDisk((int) machineEntity.getDisk());
        physicalMachine.setWorkspaceId("root");
        if (!org.apache.shiro.util.CollectionUtils.isEmpty(machineEntity.getIpArray())) {
            JsonArray jsonArray = new JsonArray();
            for (String element : machineEntity.getIpArray()) {
                jsonArray.add(element);
            }
            physicalMachine.setIpArray(jsonArray.toString());
        }
        //新发现的一律设置为未知 只有填写好带外信息 能定时获取到状态到才有该字段
        try {
            physicalMachine.setPower(getPmPower(machineEntity.getBmcIp()));
        } catch (Exception e) {
            LogUtil.error("获取 power 失败！" + JSONObject.toJSONString(e));
        }
        return physicalMachine;
    }

    private String getPmPower(String bmcIp) {
        OutBand o = outBandService.getByIp(bmcIp);
        if (o != null) {
            IPMIUtil.Account account = new IPMIUtil.Account(o.getIp(), o.getUserName(), o.getPwd());
            String ipmiResult = null;
            try {
                ipmiResult = IPMIUtil.exeCommand(account, "power status");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotBlank(ipmiResult) && ipmiResult.contains(RackHDConstants.PM_POWER_ON)) {
                return RackHDConstants.PM_POWER_ON;
            } else if (StringUtils.isNotBlank(ipmiResult) && ipmiResult.contains(RackHDConstants.PM_POWER_OFF)) {
                return RackHDConstants.PM_POWER_OFF;
            }
        }
        return RackHDConstants.PM_POWER_UNKNOWN;
    }

    @Resource
    private BareMetalMapper bareMetalMapper;

    public MachineEntity parse(LifeEvent event) {
        String bmId = event.getBareMetalId();
        BareMetal bareMetal = bareMetalMapper.selectByPrimaryKey(bmId);
        CatalogExample e1 = new CatalogExample();
        e1.createCriteria().andBareMetalIdEqualTo(bmId);
        List<Catalog> catalogs = catalogMapper.selectByExampleWithBLOBs(e1);
        long memorys;
        List<Disk> disks;
        List<Cpu> cpus;
        List<Memory> memories;

        MachineEntity en = new MachineEntity();

        memorys = 0L;
        disks = new LinkedList<>();
        cpus = new LinkedList<>();
        memories = new LinkedList<>();

        List<NetworkCard> pmNetworkCards = new ArrayList<>();
        JSONArray catalogsObj = (JSONArray) JSONArray.toJSON(catalogs);

        for (int j = 0; j < catalogsObj.size(); j++) {
            JSONObject catalogObj = catalogsObj.getJSONObject(j);
            //序列号等特征
            if ("ipmi-fru".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject fru = catalogObj.getJSONObject("data");
                if (fru.containsKey("Builtin FRU Device (ID 0)")) {
                    JSONObject fruDevice = fru.getJSONObject("Builtin FRU Device (ID 0)");
                    if (StringUtils.isAnyBlank(en.getModel(), en.getName(), en.getBrand())) {
                        en.setBrand(fruDevice.getString("Product Manufacturer"));
                        en.setModel(fruDevice.getString("Product Name"));
                        if (StringUtils.isBlank(en.getModel())) {
                            en.setModel(fruDevice.getString("Board Product"));
                            en.setName(en.getBrand() + " " + en.getModel());
                        }
                        en.setSerialNo(fruDevice.getString("Product Serial"));
                    }
                }

            }
            if ("dmi".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject dmi = catalogObj.getJSONObject("data");
                JSONArray memoryDeviceObj = dmi.getJSONArray("Memory Device");
                String memoryType = null;
                Map<String, String> extendInfo = new HashMap<>();
                List<JSONObject> memoryDetails = new ArrayList<>();
                for (int k = 0; k < memoryDeviceObj.size(); k++) {
                    JSONObject memoryObj = memoryDeviceObj.getJSONObject(k);
                    if (!"No Module Installed".equalsIgnoreCase(memoryObj.getString("Size"))) {
                        memoryType = memoryObj.getString("Type");

                        int memorySize = 0;
                        try {
                            memorySize = DiskSizeUtils.getSize(memoryObj.getString("Size"), DiskSizeUtils.Unit.GB).intValue();
                        } catch (Exception e) {
                            LogUtil.error(String.format("执行内存获取失败!nodeId:%s", bmId));
                        }

                        memorys += memorySize;
                        JSONObject memoryDetail = new JSONObject();
                        memoryDetail.put("memorySize", memorySize);
                        memoryDetail.put("memoryType", memoryType);
                        memoryDetails.add(memoryDetail);

                        //内存
                        Memory memory1 = new Memory();
                        memory1.setMemModType(memoryObj.getString("Type"));
                        memory1.setSyncTime(System.currentTimeMillis());
                        memory1.setMemModSize(String.valueOf(memorySize));
                        memory1.setMemModFrequency(memoryObj.getString("Speed").replace("MHz", ""));
                        if (memoryObj.containsKey("Minimum voltage")) {
                            memory1.setMemModMinVolt(memoryObj.getString("Minimum voltage").replace("V", "").replace(" ", ""));
                        }
                        //内存位置信息
                        if (memoryObj.containsKey("Locator") && memoryObj.getString("Locator").contains("PROC") && memoryObj.getString("Locator").contains("DIMM")) {
                            memory1.setMemCpuNum(memoryObj.getString("Locator").split("DIMM")[0].replace("PROC ", "").replace(" ", ""));
                            memory1.setMemModNum(memoryObj.getString("Locator").split("DIMM")[1].replace("PROC ", "").replace(" ", ""));
                        }
                        if (memoryObj.containsKey("Serial Number")) {
                            memory1.setSn(memoryObj.getString("Serial Number"));
                        }
                        memory1.setMemModPartNum(memoryObj.getString("Part Number"));
                        memories.add(memory1);
                    }
                }
                extendInfo.put("memoryDetails", memoryDetails.toString());
                en.setExtendInfo(extendInfo);
                en.setMemoryType(memoryType);

                JSONObject chassisInfo = dmi.getJSONObject("Chassis Information");
                if (StringUtils.isBlank(en.getBrand())) {
                    en.setBrand(chassisInfo.getString("Manufacturer"));
                    if ("Dell Inc.".equalsIgnoreCase(en.getBrand())) {
                        en.setBrand(PluginConstants.DELL);
                    }
                }
                if (StringUtils.isBlank(en.getSerialNo())) {
                    en.setSerialNo(chassisInfo.getString("Serial Number"));
                    if ("Inspur".equalsIgnoreCase(en.getBrand())) {
                        en.setSerialNo(dmi.getJSONObject("System Information").getString("Serial Number"));
                    }
                }
                if (StringUtils.equalsIgnoreCase("N/A", en.getSerialNo())) {
                    en.setSerialNo(dmi.getJSONObject("System Information").getString("Serial Number"));
                }
                if (StringUtils.isBlank(en.getModel())) {
                    en.setModel(dmi.getJSONObject("System Information").getString("Product Name"));
                }
                //cpu

                Object processor = dmi.getObject("Processor Information", Object.class);
                if (processor instanceof JSONArray) {
                    JSONArray cpuInfo = (JSONArray) processor;
                    en.setCore(Optional.ofNullable(cpuInfo.getJSONObject(0).getInteger("Core Count")).orElse(1));
                    en.setThread(Optional.ofNullable(cpuInfo.getJSONObject(0).getInteger("Thread Count")).orElse(2));
                    en.setCpuFre(cpuInfo.getJSONObject(0).getString("Current Speed"));
                    en.setCpuType(cpuInfo.getJSONObject(0).getString("Version"));

                    for (int l = 0; l < cpuInfo.size(); l++) {
                        if ("Not Specified".equalsIgnoreCase(cpuInfo.getJSONObject(l).getString("Version"))) {
                            continue;
                        }
                        Cpu cpu = new Cpu();
                        cpu.setProcNumThreads(cpuInfo.getJSONObject(l).getString("Thread Count"));
                        cpu.setSyncTime(System.currentTimeMillis());
                        cpu.setProcSpeed(cpuInfo.getJSONObject(l).getString("Current Speed").replace("MHz", "").replace(" ", ""));
                        cpu.setProcSocket(cpuInfo.getJSONObject(l).getString("Socket Designation").replace("Proc", "").replace("CPU", "").replace(" ", ""));
                        cpu.setProcNumCoresEnabled(cpuInfo.getJSONObject(l).getString("Core Enabled"));
                        cpu.setProcNumCores(cpuInfo.getJSONObject(l).getString("Core Count"));
                        cpu.setProcName(cpuInfo.getJSONObject(l).getString("Version"));
                        cpus.add(cpu);
                    }
                    en.setCpu(cpus.size());
                } else {
                    JSONObject cpuInfo = (JSONObject) processor;
                    en.setCpu(1);
                    en.setCore(Optional.ofNullable(cpuInfo.getInteger("Core Count")).orElse(1));
                    en.setThread(Optional.ofNullable(cpuInfo.getInteger("Thread Count")).orElse(2));
                    en.setCpuFre(cpuInfo.getString("Current Speed"));
                    en.setCpuType(cpuInfo.getString("Version"));

                    Cpu cpu = new Cpu();
                    cpu.setProcNumThreads(cpuInfo.getString("Thread Count"));
                    cpu.setSyncTime(System.currentTimeMillis());
                    cpu.setProcSpeed(cpuInfo.getString("Current Speed").replace("MHz", "").replace(" ", ""));
                    cpu.setProcSocket(cpuInfo.getString("Socket Designation").replace("Proc", "").replace("CPU", "").replace(" ", ""));
                    cpu.setProcNumCoresEnabled(cpuInfo.getString("Core Enabled"));
                    cpu.setProcNumCores(cpuInfo.getString("Core Count"));
                    cpu.setProcName(cpuInfo.getString("Version"));
                    cpus.add(cpu);
                }
            }

            if ("bmc".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject bmcData = catalogObj.getJSONObject("data");
                en.setBmcIp(bmcData.getString("IP Address"));
                en.setBmcMac(bmcData.getString("MAC Address"));
            }
            //兼容浪潮8480M4
            if ("0.0.0.0".equalsIgnoreCase(en.getBmcIp()) && "bmc-8".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject bmcData = catalogObj.getJSONObject("data");
                en.setBmcIp(bmcData.getString("IP Address"));
                en.setBmcMac(bmcData.getString("MAC Address"));
            }
            //dell等使用megaraid卡的机型磁盘
            if ("megaraid-controllers".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject diskData = catalogObj.getJSONObject("data");
                JSONArray controllers = diskData.getJSONArray("Controllers");

                for (int k = 0; k < controllers.size(); k++) {
                    JSONObject conObj = controllers.getJSONObject(k);
                    if (conObj.containsKey("Response Data")) {
                        JSONObject pdInfo = conObj.getJSONObject("Response Data");
                        JSONArray pdList = pdInfo.getJSONArray("PD LIST");
                        JSONArray vdList = pdInfo.getJSONArray("VD LIST");
                        // DG / VirtualDisk  0 : VD0 , 1: VD1
                        Map<String, JSONObject> vdMap = new HashMap<>();
                        if (CollectionUtils.isNotEmpty(vdList)) {
                            vdList.forEach(v -> {
                                vdMap.put(((JSONObject) v).getString("DG/VD").split("/")[0], (JSONObject) v);
                            });
                        }
                        for (int l = 0; l < pdList.size(); l++) {
                            JSONObject pdObj = pdList.getJSONObject(l);
                            Disk pd = new Disk();
                            String eId = pdObj.getString("EID:Slt").split(":")[0];
                            //有些机型出厂不自带 RAID 卡 而是共用或者拆机使用，这个值获取不到
                            pd.setEnclosureId(StringUtils.isBlank(eId) ? 32 : Integer.valueOf(eId));
                            //暂时只支持一块raid卡 所以写死为0
                            pd.setControllerId(0);
                            pd.setDrive(pdObj.getString("EID:Slt").split(":")[1]);
                            pd.setRaid(vdMap.get(pdObj.getString("DG")) == null ? null : vdMap.get(pdObj.getString("DG")).getString("TYPE"));
                            pd.setSize(DiskUtils.getDiskManufactorValue(pdObj.getString("Size")));
                            pd.setType(pdObj.getString("Intf"));
                            pd.setVirtualDisk(vdMap.get(pdObj.getString("DG")) == null ? null : vdMap.get(pdObj.getString("DG")).getString("Name"));
                            pd.setSyncTime(catalogObj.getLong("createTime"));

                            disks.add(pd);
                        }
                    }
                }
            }
            //浪潮磁盘
            if ("adaptecraid-controllers".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONArray diskData = catalogObj.getJSONArray("data");
                for (int l = 0; l < diskData.size(); l++) {
                    JSONObject pdObj = diskData.getJSONObject(l);
                    Disk pd = new Disk();
                    //暂时只支持一块raid卡 所以写死为1
                    pd.setControllerId(1);
                    pd.setRaid(pdObj.getString("RaidLevel"));
                    pd.setSize((int) Math.ceil(Integer.valueOf(pdObj.getString("Total Size").replace("MB", "").replace(" ", "")) / 1024 / 0.931) + " GB");
                    pd.setType("NO".equalsIgnoreCase(pdObj.getString("SSD")) ? "HDD" : "SSD");
                    pd.setManufactor(pdObj.getString("Vendor"));
                    if (pdObj.containsKey("EnclosureId")) {
                        pd.setEnclosureId(pdObj.getInteger("EnclosureId"));
                    } else {
                        if (pdObj.containsKey("Reported Location"))
                            pd.setEnclosureId(Integer.valueOf(pdObj.getString("Reported Location").split("Enclosure")[1].split(",")[0].replace(" ", "")));
                    }
                    if (pdObj.containsKey("EnclosureId")) {
                        pd.setEnclosureId(pdObj.getInteger("EnclosureId"));
                    } else {
                        if (pdObj.containsKey("Reported Location"))
                            pd.setEnclosureId(Integer.valueOf(pdObj.getString("Reported Location").split("Enclosure")[1].split(",")[0].replace(" ", "")));
                    }

                    if (pdObj.containsKey("Slot")) {
                        pd.setDrive(pdObj.getString("Slot"));
                    } else {
                        if (pdObj.containsKey("Reported Location"))
                            pd.setDrive(pdObj.getString("Reported Location").split("Slot")[1].split("\\(")[0].replace(" ", ""));
                    }

                    pd.setSyncTime(catalogObj.getLong("createTime"));
                    disks.add(pd);
                }
            }
            //hp 磁盘
            if ("hpssaraid-controllers".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONArray diskData = catalogObj.getJSONArray("data");
                for (int k = 0; k < diskData.size(); k++) {
                    JSONObject disk = diskData.getJSONObject(k);
                    Disk pd = new Disk();
                    pd.setEnclosureId(0);
                    //暂时只支持一块raid卡 所以写死为0
                    pd.setControllerId(0);
                    //拼装成可以直接给RackHD shell使用的形式
                    pd.setDrive(disk.getString("Port") + ":" + disk.getString("Box") + ":" + disk.getString("Bay"));
                    pd.setRaid(disk.getString("raid"));
                    pd.setSize(disk.getString("Size"));
                    pd.setType(disk.getString("Interface Type"));
                    pd.setSn(disk.getString("Serial Number"));
                    pd.setModel(disk.getString("Model"));
                    pd.setVirtualDisk(null);
                    pd.setSyncTime(catalogObj.getLong("createTime"));
                    disks.add(pd);
                }
            }
            if ("lshw".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject networkData = catalogObj.getJSONObject("data");
                JSONArray childrens = networkData.getJSONArray("children");
                for (int k = 0; k < childrens.size(); k++) {
                    JSONObject children = childrens.getJSONObject(k);
                    if (children.getString("id").equalsIgnoreCase("core")) {
                        JSONArray childs = children.getJSONArray("children");
                        for (int l = 0; l < childs.size(); l++) {
                            JSONObject child = childs.getJSONObject(l);
                            if (child.getString("id").equalsIgnoreCase("pci")) {
                                JSONArray pcis = child.getJSONArray("children");
                                for (int m = 0; m < pcis.size(); m++) {
                                    JSONObject pci = pcis.getJSONObject(m);
                                    if (pci.getString("id").contains("pci") && pci.get("children") != null) {
                                        JSONArray networks = pci.getJSONArray("children");
                                        for (int n = 0; n < networks.size(); n++) {
                                            JSONObject network = networks.getJSONObject(n);
                                            if (network.getString("id").contains("network")) {
                                                NetworkCard pmNetworkCard = new NetworkCard();
                                                if (network.getString("logicalname") != null) {
                                                    pmNetworkCard.setNumber(network.getString("logicalname"));
                                                }
                                                if (network.getString("serial") != null) {
                                                    pmNetworkCard.setMac(network.getString("serial"));
                                                }
                                                if (network.getJSONObject("configuration").getString("ip") != null) {
                                                    pmNetworkCard.setIp(network.getJSONObject("configuration").getString("ip"));
                                                }
                                                pmNetworkCard.setSyncTime(catalogObj.getLong("createTime"));
                                                pmNetworkCards.add(pmNetworkCard);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if ("ohai".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject networkData = catalogObj.getJSONObject("data");
                JSONObject networkObj = networkData.getJSONObject("network");
                JSONObject interfaceObj = networkObj.getJSONObject("interfaces");
                Set<Map.Entry<String, Object>> cardsIdSet = interfaceObj.entrySet();
                for (Map.Entry<String, Object> entry : cardsIdSet) {
                    if (entry.getKey().startsWith("e")) {
                        NetworkCard pmNetworkCard = new NetworkCard();
                        pmNetworkCard.setNumber(entry.getKey());
                        JSONObject addressObj = JSONObject.parseObject(((JSONObject) entry.getValue()).getString("addresses"));
                        pmNetworkCard.setMac(addressObj.keySet().stream().filter(s -> s.matches("(\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})")).findFirst().get());
                        pmNetworkCard.setSyncTime(catalogObj.getLong("createTime"));
                        pmNetworkCards.add(pmNetworkCard);
                    }
                }
            }

            if ("smart".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONArray smartArr = catalogObj.getJSONArray("data");
                for (int k = 0; k < smartArr.size(); k++) {
                    JSONObject smartObj = smartArr.getJSONObject(k);
                    JSONObject smartDisk = smartObj.getJSONObject("SMART");
                    if (smartDisk.containsKey("Identity")) {
                        JSONObject identityObj = smartDisk.getJSONObject("Identity");
                        if (identityObj.containsKey("User Capacity")) {
                            String diskCapacityStr = identityObj.getString("User Capacity").split(" ")[0].replace(",", "");
                            if (identityObj.getString("User Capacity").contains("bytes")) {
                                en.setDisk(Long.parseLong(diskCapacityStr) / 1024 / 1024 / 1024);
                            }
                        }
                    }
                }
            }
        }
        en.setNetworkCards(getLatestCards(pmNetworkCards));
        en.setMemory(memorys);
        en.setMemories(memories);
        en.setCpus(cpus);
        en.setNodeId(bmId);
        en.setDisks(getLatestDisk(disks));
        en.setPxeMac(Optional.ofNullable(bareMetal).orElse(new BareMetal()).getPxeMac());
        return en;
    }
}
