package io.rackshift.plugin.hp;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.metal.sdk.AbstractMetalProvider;
import io.rackshift.metal.sdk.MetalPlugin;
import io.rackshift.metal.sdk.MetalPluginException;
import io.rackshift.metal.sdk.constants.BareMetalConstants;
import io.rackshift.metal.sdk.constants.ProtocolEnum;
import io.rackshift.metal.sdk.constants.ResourceTypeConstants;
import io.rackshift.metal.sdk.model.*;
import io.rackshift.metal.sdk.model.request.IPMIRequest;
import io.rackshift.metal.sdk.model.request.IPMISnmpRequest;
import io.rackshift.metal.sdk.util.*;
import io.rackshift.plugin.hp.model.*;
import io.rackshift.plugin.hp.utils.HpHttpFutureUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.rackshift.metal.sdk.constants.RackHDConstants.workflowPostUrl;


@MetalPlugin
public class HpMetalProvider extends AbstractMetalProvider {


    private static final String loginUrl = "https://%s/json/login_session";
    private static final String overviewUrl = "https://%s/json/overview";
    private static final String mem_infoUrl = "https://%s/json/mem_info";
    private static final String proc_infoUrl = "https://%s/json/proc_info";
    private static final String health_drivesUrl = "https://%s/json/health_drives";
    private static final String health_phy_drivesUrl = "https://%s/json/health_phy_drives";
    private static final String phy_nic_infoUrl = "https://%s/json/phy_nic_info";
    private static final String power_infoUrl = "https://%s/dispatch/DEFAULT";
    private static ConcurrentHashMap<String, Map<String, String>> headersMap;
    private static final String temperatureLocale = "1.3.6.1.4.1.232.6.2.6.8.1.3";
    private static final String temperatureValue = "1.3.6.1.4.1.232.6.2.6.8.1.4";
    private static final String powerSupplyLocale = "1.3.6.1.4.1.232.6.2.9.3.1.4";
    private static final String fanLocale = "1.3.6.1.4.1.232.6.2.6.7.1.9";
    private static final String duiskStatusLocale = "1.3.6.1.4.1.232.3.2.5.1.1.6";
    private static Map<String, String> snmpValueMap = new HashMap<String, String>();
    private static JSONObject powerPayload;

    static {
        snmpValueMap.put("6", cpuTemperature);
        snmpValueMap.put("7", memoryTemperature);
        snmpValueMap.put("4", mainBoardTemperature);
        snmpValueMap.put("10", powerSupplyTemperature);
        snmpValueMap.put("5", powerStatus);
        snmpValueMap.put("30", fanStatus);
        headersMap = new ConcurrentHashMap<>();
        powerPayload = JSON.parseObject("{\n" +
                "    \"filter\": {\n" +
                "        \"license_features\": {\n" +
                "            \"powerreg\": 1\n" +
                "        },\n" +
                "        \"power_cap\": {\n" +
                "            \"power_cap_ability_disabled\": 0\n" +
                "        },\n" +
                "        \"power_capabilities\": {\n" +
                "            \"has_advcap\": 1,\n" +
                "            \"cap_calib\": \"ok\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"query\": {\n" +
                "        \"power_capabilities\": {\n" +
                "            \"max_cap\": \"SUM\",\n" +
                "            \"_0_pct_cap\": \"SUM\",\n" +
                "            \"_100_pct_cap\": \"SUM\"\n" +
                "        },\n" +
                "        \"power_summary\": {\n" +
                "            \"power_supply_capacity\": \"SUM\",\n" +
                "            \"last_avg_pwr_accum\": \"SUM\"\n" +
                "        },\n" +
                "        \"fedcap/DEFAULT\": {\n" +
                "            \"cap\": \"SUM\",\n" +
                "            \"contributed_power\": \"SUM\",\n" +
                "            \"group_caps\": {\n" +
                "                \"DEFAULT\": \"SUM\"\n" +
                "            },\n" +
                "            \"throttle\": \"COUNT\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"session_key\": \"c28329e9896ddb3a76ec5612e49ecacb\"\n" +
                "}");
    }

    public HpMetalProvider() {
        super.name = "hp-metal-plugin";
    }


    @Override
    public Map<String, String> getHeader(String ip) {
        return headersMap.get(ip);
    }

    public PluginResult login(String ipmiRequestStr) throws MetalPluginException {
        IPMIRequest ipmiRequest = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
        checkIPMIParameter(ipmiRequest);
        String ip = ipmiRequest.getHost();
        String userName = ipmiRequest.getUserName();
        String password = ipmiRequest.getPwd();
        if (headersMap.get(ip) == null) {
            JSONObject request = new JSONObject();
            request.put("method", "login");
            request.put("user_login", userName);
            request.put("password", password);
            try {
                JSONObject res = JSONObject.parseObject(HpHttpFutureUtils.postHttps(String.format(loginUrl, ip), request.toJSONString(), "application/json"));

                if (res.containsKey("session_key")) {
                    Map cookieMap = new HashMap();
                    cookieMap.put("Cookie", "sessionKey=" + res.getString("session_key"));
                    headersMap.put(ip, cookieMap);
                    return PluginResult.success();
                }
                return PluginResult.error("登录失败！");
            } catch (Exception e) {
                LogUtil.error(String.format("爬取惠普ip：{%s}时，获取sessionKey失败！e：{%s}", ip, ExceptionUtils.getExceptionDetail(e)));
                return PluginResult.error(e.getMessage());
            }
        }
        return PluginResult.success();
    }

    public PluginResult logout(String ipmiRequestStr) {
        IPMISnmpRequest request = gson.fromJson(ipmiRequestStr, IPMISnmpRequest.class);
        if (headersMap.get(request.getHost()) != null) {
            String ip = request.getHost();
            JSONObject r = new JSONObject();
            r.put("method", "logout");
            r.put("session_key", headersMap.get(ip).get("Cookie").split("=")[1]);
            try {
                String s = HpHttpFutureUtils.postHttps(String.format(loginUrl, ip), r.toJSONString(), "application/json");
                headersMap.remove(request.getHost());
                if (StringUtils.isBlank(s)) {
                    return PluginResult.success("登出成功！");
                }
            } catch (Exception e) {
                LogUtil.error(String.format("登出惠普ip：{%s}失败！e：{%s}", ip, ExceptionUtils.getExceptionDetail(e)));
                PluginResult.error(e.getMessage());
            }
        }
        return PluginResult.success();
    }

    public MachineEntity getMachineEntity(String ipmiRequestStr) throws MetalPluginException {
        IPMIRequest ipmiRequest = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
        checkIPMIParameter(ipmiRequest);
        MachineEntity machineEntity = new MachineEntity();
        List<Disk> diskList = new LinkedList<>();
        List<HpCpuDTO> cpuDTOS = new LinkedList<>();
        List<HpMemoryDTO> memoryDTOS = new LinkedList<>();
        List<NetworkCard> networkCards = new LinkedList<>();
        long now = System.currentTimeMillis();
        String ip = ipmiRequest.getHost();

        if (login(ipmiRequestStr).isSuccess()) {

            Map headers = headersMap.get(ip);
            try {
                String result = judgeExpire(String.format(overviewUrl, ip), headers);

                JSONObject resObj = JSON.parseObject(result);
                machineEntity.setSerialNo(Optional.ofNullable(resObj.getString("serial_num")).isPresent() ? resObj.getString("serial_num").trim() : null);
                machineEntity.setInstanceUuid(Optional.ofNullable(resObj.getString("uuid")).isPresent() ? resObj.getString("uuid").trim() : null);
                machineEntity.setModel(Optional.ofNullable(resObj.getString("product_name")).isPresent() ? resObj.getString("product_name").trim() : null);
                machineEntity.setName("HP " + machineEntity.getModel());
                machineEntity.setBmcIp(Optional.ofNullable(resObj.getString("ip_address")).isPresent() ? resObj.getString("ip_address").trim() : null);
                machineEntity.setBrand("HP");
                machineEntity.setProcPwrPerf(Optional.ofNullable(resObj.getString("power")).isPresent() ? resObj.getString("power").trim() : null);

                //cpu
                result = judgeExpire(String.format(proc_infoUrl, ip), headers);
                resObj = JSON.parseObject(result);
                if (resObj.containsKey("processors")) {
                    JSONArray cpus = resObj.getJSONArray("processors");
                    cpus.forEach(m -> {
                        HpCpuDTO m1 = gson.fromJson(m.toString(), HpCpuDTO.class);
                        cpuDTOS.add(m1);
                    });

                    if (cpuDTOS.size() > 0) {
                        machineEntity.setCpu(cpuDTOS.size());
                        machineEntity.setCpuFre(cpuDTOS.get(0).getProcSpeed() + "");
                        machineEntity.setCpuType(cpuDTOS.get(0).getProcName());
                        machineEntity.setCore(cpuDTOS.get(0).getProcNumCores());
                        machineEntity.setThread(cpuDTOS.get(0).getProcNumThreads());
                    }
                }

                //内存
                result = judgeExpire(String.format(mem_infoUrl, ip), headers);
                resObj = JSON.parseObject(result);

                if (resObj.containsKey("mem_modules")) {
                    JSONArray memorys = resObj.getJSONArray("mem_modules");
                    memorys.forEach(m -> {
                        HpMemoryDTO m1 = gson.fromJson(m.toString(), HpMemoryDTO.class);
                        if (m1.getMemModSize() == 0) {
                            return;
                        }
                        memoryDTOS.add(m1);
                    });
                    //内存存两份一份存extendInfo作为直接展示 另一份存另一个表
                    if (memoryDTOS.size() > 0) {
                        List<JSONObject> memoryDetails = new ArrayList<>();
                        memoryDTOS.forEach(memory -> {
                            JSONObject memoryDetail = new JSONObject();
                            memoryDetail.put("memorySize", new BigDecimal(memory.getMemModSize()).divide(new BigDecimal("1024"), 0, BigDecimal.ROUND_HALF_UP).intValue());
                            memoryDetail.put("memoryType", memory.getMemModType().replace("MEM_DIMM_", ""));
                            machineEntity.setMemoryType(memory.getMemModType().replace("MEM_DIMM_", ""));
                            memoryDetails.add(memoryDetail);
                        });
                        HashMap<String, String> extendInfo = new HashMap<>();
                        extendInfo.put("memoryDetails", memoryDetails.toString());
                        machineEntity.setExtendInfo(extendInfo);
                        machineEntity.setMemory(memoryDTOS.stream().mapToInt(m -> m.getMemModSize()).sum() / 1024);
                    }
                }

                //逻辑磁盘
                result = judgeExpire(String.format(health_drivesUrl, ip), headers);
                HpLogDrivesDTO hpLogDrivesDTO = gson.fromJson(result, HpLogDrivesDTO.class);
                Map<Integer, String> phRaidMap = new HashMap<>();
                if (hpLogDrivesDTO != null && hpLogDrivesDTO.getLogDriveArrays().size() > 0) {
                    hpLogDrivesDTO.getLogDriveArrays().forEach(d -> {
                        d.getLogicalDrives().forEach(d1 -> {
                            d1.getPhysicalDrives().forEach(d2 -> {
                                phRaidMap.put(d2, d1.getFltTol());
                            });
                        });
                    });
                }

                //物理磁盘
                result = judgeExpire(String.format(health_phy_drivesUrl, ip), headers);
                HpPhyDrivesDTO hpPhyDrivesDTO = gson.fromJson(result, HpPhyDrivesDTO.class);
                if (hpPhyDrivesDTO != null && hpPhyDrivesDTO.getPhyDriveArrays() != null && hpPhyDrivesDTO.getPhyDriveArrays().size() > 0) {

                    hpPhyDrivesDTO.getPhyDriveArrays().forEach(d -> {
                        d.getPhysicalDrives().forEach(d1 -> {
                            Disk disk = new Disk();
                            disk.setModel(d1.getModel());
                            disk.setSn(d1.getSerialNo());
                            disk.setSize(DiskUtils.getDiskManufactorValue(d1.getCapacity()));
                            String tempPhLocation[] = d1.getLocation().split(" ");
                            if (tempPhLocation.length > 5) {
                                //需要拼接这几个参数 给Raid做准备
                                disk.setDrive(tempPhLocation[1] + ":" + tempPhLocation[3] + ":" + tempPhLocation[5]);
                            } else {
                                disk.setDrive("");
                            }
                            disk.setRaid(phRaidMap.get(d1.getPhysIdx()));
                            disk.setSyncTime(now);
                            disk.setControllerId(0);
                            //暂时设置为0
                            disk.setEnclosureId(0);
                            disk.setType(d1.getDriveType() != null ? d1.getDriveType().contains("PHY") ? "SAS" : "SSD" : "SAS");
                            diskList.add(disk);
                        });
                    });
                }

                //物理网卡（不包含带外卡）
                result = judgeExpire(String.format(phy_nic_infoUrl, ip), headers);
                HpPhyNicDTO phyNicDTO = gson.fromJson(result, HpPhyNicDTO.class);
                if (phyNicDTO != null && phyNicDTO.getPhyNics().size() > 0) {
                    for (HpPhyNicDTO.PhyNicsBean phyNic : phyNicDTO.getPhyNics()) {
                        if (phyNic.getName().startsWith("iLO")) continue;
                        phyNic.getPorts().forEach(p -> {
                            NetworkCard card = new NetworkCard();
                            card.setMac(p.getMacAddr());
                            card.setNumber(Optional.ofNullable(p.getPortNum()).orElse(0).intValue() + "");
                            card.setSynTime(now);
                            networkCards.add(card);
                        });
                    }
                }
                machineEntity.setDisks(diskList);
                machineEntity.setPmNetworkCards(networkCards);
            } catch (Exception e) {
                LogUtil.error(String.format("爬取惠普ip：{%s}失败！异常为：%s", ip, ExceptionUtils.getExceptionDetail(e)));
                headersMap.remove(ip);
                return null;
            }

            logout(ipmiRequestStr);
        }

        setCPUandMemory(machineEntity, cpuDTOS, memoryDTOS, ipmiRequestStr);
        return machineEntity;
    }

    @Override
    public MachineEntity getMachineEntityThroughSNMP(String ipmiRequestStr) throws MetalPluginException {
        return null;
    }

    @Override
    public MachineEntity getMachineEntityThroughRedfish(String ipmiRequestStr) throws MetalPluginException {
        return null;
    }

    public JSONObject getRaidPayLoad(String raidConfigDTOStr) throws MetalPluginException {
        JSONObject raidPayload = JSONObject.parseObject(raidConfigDTOStr);

        JSONObject createRaid = raidPayload.getJSONObject("options").getJSONObject("create-raid");
        JSONArray raidList = new JSONArray();

        for (int i = 0; i < createRaid.getJSONArray("raidList").size(); i++) {
            JSONObject c = createRaid.getJSONArray("raidList").getJSONObject(i);
            JSONObject raidConfigObj = new JSONObject();
            raidConfigObj.put("type", getValidRaidType(c.getString("type")));
            raidConfigObj.put("drives", c.getJSONArray("drives").stream().map(s -> (String) s).sorted().collect(Collectors.joining(",")));
            raidList.add(raidConfigObj);
        }
        createRaid.put("raidList", raidList);
        return raidPayload;
    }

    @Override
    public JSONObject getDeleteRaidPayload() {
        try {
            return JSONObject.parseObject(getPageTemplate(ResourceTypeConstants.RACKHD_RAID_DEL_PAYLOAD));
        } catch (MetalPluginException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    public String getRaidWorkFlow() {
        return workflowPostUrl + "Graph.Raid.Create.HpssaRAID";
    }

    @Override
    public String getDeleteRaidWorkFlow() {
        return workflowPostUrl + "Graph.Raid.Delete.HpssaRAID";
    }

    @Override
    public String getCatalogRaidWorkFlow() {
        return workflowPostUrl + "Graph.HP.ssacli.Catalog";
    }

    /**
     * 支持的raid 0, 1+0, 1+0adm, 5, 6, 50, 60
     * 工具 hpssacli 版本 2.0.22.0
     *
     * @param raidType
     * @return
     * @throws MetalPluginException
     */
    public String getValidRaidType(String raidType) throws MetalPluginException {
        if (raidType.equalsIgnoreCase("raid10")) {
            return "1+0";
        }
        return raidType.replace("raid", "");
    }

    public Metric getMetric(String ipmiSnmpRequestStr) throws MetalPluginException {
        IPMISnmpRequest request = gson.fromJson(ipmiSnmpRequestStr, IPMISnmpRequest.class);
        checkIPMISnmpParameter(request);
        Metric f2CBareMetalMetric = new Metric();
        try {
            SnmpWorker snmpWorker = new SnmpWorker(request.getHost(), request.getCommunity(), request.getPort());
            Map<String, String> temperatureResult = snmpWorker.walk(temperatureLocale);
            Map<String, String> powerStatusResult = snmpWorker.walk(powerSupplyLocale);
            Map<String, String> fanStatusResult = snmpWorker.walk(fanLocale);
            Map<String, String> diskStatusResult = snmpWorker.walk(duiskStatusLocale);
            String ipmiSdr = IPMIUtils.exeCommand(request, "sdr");
            // 日志信息
            String selStr = IPMIUtils.exeCommand(request, "sel");
            if (StringUtils.isNotBlank(selStr)) {
                for (String s : selStr.split("\n")) {
                    if (StringUtils.isNotBlank(s) && s.contains("Percent Used")) {
                        f2CBareMetalMetric.setSelPercentUsed(Long.valueOf(s.replace("%", "")
                                .replace("\"", "")
                                .replace(" ", "")
                                .split(":")[1]));
                    }
                }
            }

            for (Map.Entry<String, String> entry : powerStatusResult.entrySet()) {
                f2CBareMetalMetric.getPowerStatus().add(unifyStatus(Integer.valueOf(entry.getValue())));
            }

            for (Map.Entry<String, String> entry : temperatureResult.entrySet()) {
                if (cpuTemperature.equalsIgnoreCase(snmpValueMap.get(entry.getValue()))) {
                    f2CBareMetalMetric.getCpuTemp().add(Integer.valueOf(snmpWorker.getAsString(temperatureValue + entry.getKey().replace(temperatureLocale, ""))));
                }
                if (memoryTemperature.equalsIgnoreCase(snmpValueMap.get(entry.getValue()))) {
                    f2CBareMetalMetric.getMemoryTemp().add(Integer.valueOf(snmpWorker.getAsString(temperatureValue + entry.getKey().replace(temperatureLocale, ""))));
                }
                if (mainBoardTemperature.equalsIgnoreCase(snmpValueMap.get(entry.getValue()))) {
                    f2CBareMetalMetric.setMainBoardTemp(Integer.valueOf(snmpWorker.getAsString(temperatureValue + entry.getKey().replace(temperatureLocale, ""))));
                }

                if (powerSupplyTemperature.equalsIgnoreCase(snmpValueMap.get(entry.getValue()))) {
                    //惠普的可能获取到比实际电源多一个电源的温度，具体原因未知
                    if (f2CBareMetalMetric.getPowerTemp().size() < f2CBareMetalMetric.getPowerStatus().size()) {
                        f2CBareMetalMetric.getPowerTemp().add(Integer.valueOf(snmpWorker.getAsString(temperatureValue + entry.getKey().replace(temperatureLocale, ""))));
                    }
                }
            }

            for (Map.Entry<String, String> entry : fanStatusResult.entrySet()) {
                f2CBareMetalMetric.getFanStatus().add(unifyStatus(Integer.valueOf(entry.getValue())));
            }
            for (Map.Entry<String, String> entry : diskStatusResult.entrySet()) {
                f2CBareMetalMetric.getDisktatus().add(unifyStatus(Integer.valueOf(entry.getValue())));
            }

            if (login(ipmiSnmpRequestStr).isSuccess()) {
                Map headers = headersMap.get(request.getHost());
                try {
                    String sessionKeyMap = (String) headers.get("Cookie");
                    JSONObject payload = (JSONObject) powerPayload.clone();
                    payload.put("session_key", sessionKeyMap.split("=")[1]);
                    String result = HpHttpFutureUtils.postHttps(String.format(power_infoUrl, request.getHost()), payload.toJSONString(), "application/json", headers);
                    HpPowerDTO powerDTO = gson.fromJson(result, HpPowerDTO.class);
//                    f2CBareMetalMetric.setPowerWatt(Integer.valueOf(powerDTO.getPowerSummary().getLastAvgPwrAccum()));
                    f2CBareMetalMetric.getPowerWatt().add(powerDTO.getPowerSummary().getLastAvgPwrAccum());
                } catch (Exception e) {
                    LogUtil.error("获取惠普功耗失败！", ExceptionUtils.getExceptionDetail(e));
                    headersMap.remove(request.getHost());
                }
            }
            return f2CBareMetalMetric;
        } catch (Exception e) {
            LogUtil.error("获取惠普监控失败！", ExceptionUtils.getExceptionDetail(e));
            headersMap.remove(request.getHost());
        }
        return null;
    }

    @Override
    public List<ProtocolEnum> getSupportedProtocol() {
        return null;
    }

    private Integer unifyStatus(Integer valueOf) {
        if (valueOf == 2) {
            return BareMetalConstants.HEALTHY;
        }
        return BareMetalConstants.ERROR;
    }

    String judgeExpire(String url, Map headers) throws IOException {
        String response = HpHttpFutureUtils.getHttps(url, headers);
        if (response.contains("JS_ERR_LOST_SESSION")) {
            MetalPluginException.throwException("惠普登录失效！");
            LogUtil.error(String.format("惠普登录失效！url:%s, 返回结果：%s", url, response));
        }
        return response;
    }

    private void setCPUandMemory(MachineEntity machineEntity, List<HpCpuDTO> cpuDTOS, List<HpMemoryDTO> memoryDTOS, String ipmiRequestStr) {
        List<Cpu> cpus = new LinkedList<>();
        List<Memory> memories = new LinkedList<>();

        long now = System.currentTimeMillis();
        cpuDTOS.forEach(c -> {
            Cpu cpu = new Cpu();
            cpu.setProcName(Optional.ofNullable(c.getProcName()).orElse("unknown"));
            cpu.setProcNumCores(String.valueOf(c.getProcNumCores()));
            cpu.setProcNumCoresEnabled(String.valueOf(c.getProcNumCoresEnabled()));
            cpu.setProcNumThreads(String.valueOf(c.getProcNumThreads()));
            cpu.setProcNumL1cache(String.valueOf(c.getProcNumL1cache()));
            cpu.setProcNumL2cache(String.valueOf(c.getProcNumL2cache()));
            cpu.setProcNumL3cache(String.valueOf(c.getProcNumL3cache()));
            cpu.setProcMemTechnology(Optional.ofNullable(c.getProcMemTechnology()).orElse(""));
            cpu.setProcSocket(Optional.ofNullable(c.getProcSocket()).orElse("").replace("Proc", "").replace(" ", ""));
            cpu.setProcSpeed(String.valueOf(c.getProcSpeed()));
            cpu.setProcStatus(c.getProcStatus());
            cpu.setSyncTime(now);
            cpus.add(cpu);
        });

        //Rest方式获取不到序列号 这里再次使用一次IPMI获取序列号
        IPMIRequest account = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
        String ipmiResult = null;
        Map<String, String> cpuSlotMap = new HashMap<>();

        try {
            ipmiResult = IPMIUtils.exeCommand(account, "fru");
            List<HpIPMIMemoryDTO> hpIPMIMemoryDTOS = getIPMIMemoryDTOs(ipmiResult);
            Pattern p = Pattern.compile("\\d+");
            StringBuffer keySb = new StringBuffer();
            hpIPMIMemoryDTOS.forEach(m -> {
                Matcher matcher = p.matcher(m.get_$FRUDeviceDescription292());
                int i = 0;
                while (matcher.find()) {
                    if (i++ < 2)
                        keySb.append(matcher.group());
                    else
                        break;
                }
                cpuSlotMap.put(keySb.toString(), m.get_$SerialNumber301());
                keySb.delete(0, keySb.length());
            });
        } catch (Exception e) {
            LogUtil.error(String.format("惠普：{%s}执行IPMI命令获取内存失败！", account.getHost()));
        }

        memoryDTOS.forEach(c -> {
            Memory memory = new Memory();
            memory.setMemCpuNum(String.valueOf(c.getMemCpuNum()));
            memory.setMemModFrequency(String.valueOf(c.getMemModFrequency()));
            memory.setMemModMinVolt(String.valueOf(c.getMemModMinVolt() / 1000));
            memory.setMemModType(Optional.ofNullable(c.getMemModType()).orElse("").replace("MEM_DIMM_", ""));
            memory.setMemModPartNum(Optional.ofNullable(c.getMemModPartNum()).orElse(""));
            memory.setMemModSize(String.valueOf(c.getMemModSize() / 1024));
            memory.setMemModNum(String.valueOf(c.getMemModNum()));
            memory.setSn(cpuSlotMap.get(memory.getMemCpuNum() + memory.getMemModNum()));
            memory.setSyncTime(now);
            memories.add(memory);
        });
        machineEntity.setPmCpus(cpus);
        machineEntity.setPmMemories(memories);
    }

    private static List<HpIPMIMemoryDTO> getIPMIMemoryDTOs(String ipmiResult) {
        String[] marr = ipmiResult.split("\n");
        final List<HpIPMIMemoryDTO> list = new LinkedList<>();
        final StringBuffer sb = new StringBuffer();
        Arrays.asList(marr).forEach(m -> {
            if (StringUtils.isBlank(m)) {
                JSONObject tempObj = IPMIUtils.transform(sb.toString());
                if (tempObj.keySet().size() == 16 || tempObj.keySet().size() == 21) {
                    list.add(gson.fromJson(IPMIUtils.transform(sb.toString()).toJSONString(), HpIPMIMemoryDTO.class));
                }
                sb.delete(0, sb.length());
                return;
            }
            sb.append(m).append("\n");
        });
        JSONObject tempObj = IPMIUtils.transform(sb.toString());
        if (tempObj.keySet().size() == 16 || tempObj.keySet().size() == 21) {
            list.add(gson.fromJson(IPMIUtils.transform(sb.toString()).toJSONString(), HpIPMIMemoryDTO.class));
        }
        return list;
    }
}
