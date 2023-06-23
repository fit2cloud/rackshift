package io.rackshift.plugin.dell.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.rackshift.metal.sdk.MetalPluginException;
import io.rackshift.metal.sdk.model.*;
import io.rackshift.metal.sdk.util.DiskUtils;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.metal.sdk.util.LogUtil;
import io.rackshift.plugin.dell.model.DellCpuDTO;
import io.rackshift.plugin.dell.model.DellMemoryDTO;
import io.rackshift.plugin.dell.model.DellPdDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.util.UriEncoder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IDrac7RestSpider implements IIDRACRestAPI {

    private static final String url = "https://%s/%s";
    private static final String overviewUrl = "https://%s/data?get=pwState,sysDesc,sysRev,hostName,osName,svcTag,biosVer,fwVersion,iDSDMVersion,v4Enabled,v4IPAddr,v6Enabled,v6LinkLocal,v6Addr,v6SiteLocal,macAddr,dnsDomain,devLocInfo,osVersion,LCCfwVersion,intrusion,powerSupplies,RecentEventLogEntries,GetRecentWorknotes";
    private static final String getCookieUrl = "https://%s/login.html";
    private static final String getSt1Url = "https://%s/data/login";
    private static final String logoutUrl = "https://%s/data/logout";

    private static final String getCPUUrl = "https://%s/sysmgmt/2012/server/processor";
    private static final String getMemoryUrl = "https://%s/sysmgmt/2012/server/memory";
    private static final String getAllDiskUrl = "https://%s/sysmgmt/2010/storage/pdisk";
    private static final String getDiskUrl = "https://%s/sysmgmt/2010/storage/pdisk?keys=%s";

    private static final String getAllVirtualDiskUrl = "https://%s/sysmgmt/2010/storage/vdisk";
    private static final String getVirtualDiskUrl = "https://%s/sysmgmt/2010/storage/vdisk?keys=%s";
    private static final String getVirtualDiskPhyDiskUrl = "https://%s/sysmgmt/2010/storage/pdisk?vdisk=%s";
    private static final String getNetWorkUrl = "https://%s/sysmgmt/2010/storage/controller";
    private static final String getPowerWattUrl = "https://%s/data?get=powermonitordata,powergraphdata,psRedundancy,pwState,pbtEnabled,activePLPolicy,activePLimit,pwrBudgetVal,powerSupplies,pbMaxWatts,";
    private static ConcurrentHashMap<String, Map<String, String>> headersMap;
    private static ConcurrentHashMap<String, LocalDateTime> timeoutMap;
    private static Gson gson;
    private static DateTimeFormatter dateTimeFormatter;

    static {
        try {
            headersMap = new ConcurrentHashMap<>();
            timeoutMap = new ConcurrentHashMap<>();
            gson = new Gson();
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean login(String ip, String userName, String password) {
        CloseableHttpClient httpClient = null;
        try {
            if (headersMap.get(ip) != null) {
                return true;
            }
            if (timeoutMap.get(ip) != null && timeoutMap.get(ip).isAfter(LocalDateTime.now())) {
                LogUtil.error(String.format("爬取Dell ip：{%s}硬件接口当前时间%s休息至%s。。。", ip, LocalDateTime.now().format(dateTimeFormatter), timeoutMap.get(ip).format(dateTimeFormatter)));
                return false;
            }
            httpClient = HttpFutureUtils.getOneHttpClient();
            HttpGet httpGet = new HttpGet(String.format(getCookieUrl, ip));
            Header[] cookies = httpClient.execute(httpGet).getAllHeaders();
            Map<String, String> headers = new HashMap<>();
            headers.put("Cookie", getCookie(cookies, "SET-COOKIE"));
            String entityString = String.format("user=%s&password=%s", userName, password);
            String XMLResponse = HttpFutureUtils.postHttps(String.format(getSt1Url, ip), entityString, "application/x-www-form-urlencoded");
            LogUtil.info(String.format("爬取Dell idrac7 %s登录接口返回结果：%s", ip, XMLResponse));
            SAXReader saxReader = new SAXReader();
            Element responseElement = saxReader.read(new ByteArrayInputStream(XMLResponse.getBytes("UTF-8"))).getRootElement();
            if (responseElement.element("authResult") != null && "1".equalsIgnoreCase(responseElement.element("authResult").getText())) {
                LogUtil.error(String.format("爬取Dell idrac7 %s硬件信息时登录失败！可能是账号密码不对！%s", ip, entityString));
                return false;
            }
            if (responseElement.element("forwardUrl") == null) {
                LogUtil.error(String.format("爬取Dell idrac7 %s硬件信息时，获取重定向地址失败！可能是达到了最大爬取次数!休息1小时！", ip));
                removeSession(ip);
                return false;
            }
            String forwardUrl = saxReader.read(new ByteArrayInputStream(XMLResponse.getBytes("UTF-8"))).getRootElement().element("forwardUrl").getText();
            LogUtil.info(String.format("爬取Dell idrac7 %s登录接口返回结果forwardUrl：%s", ip, forwardUrl));
            String pageST2 = HttpFutureUtils.getHttps(String.format(url, ip, forwardUrl), headers);
            String tempArr[] = pageST2.split("\n");
            String ST2 = null;
            for (int i = 0; i < tempArr.length; i++) {
                if (tempArr[i].split("=").length != 2) continue;
                if (tempArr[i].split("=")[0].trim().equalsIgnoreCase("var TOKEN_VALUE")) {
                    ST2 = tempArr[i].split("=")[1].trim();
                    break;
                }
            }
            headers.put("ST2", ST2.replace("\"", "").replace(";", ""));
            headers.put("X_SYSMGMT_OPTIMIZE", "true");
            headersMap.put(ip, headers);
            return true;
        } catch (Exception e) {
            LogUtil.error(String.format("爬取Dell idrac7 %s硬件信息时，获取重定向地址失败！可能是达到了最大爬取次数！清空缓存！e:%s", ip, e));
            removeSession(ip);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean logout(String ip) {
        Map headers = headersMap.get(ip);
        if (headers != null) {
            String result = HttpFutureUtils.getHttps(String.format(logoutUrl, ip), headers);
            if (StringUtils.isNotBlank(result)) {
                removeSession(ip);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean closeAllVirtualSession(String ip) {
        return false;
    }

    private void removeSession(String ip) {
        headersMap.remove(ip);
    }

    static String getCookie(Header[] header, String key) {
        for (Header h : header) {
            if (h.getName().equalsIgnoreCase(key)) {
                return h.getValue();
            }
        }
        return null;
    }

    @Override
    public MachineEntity getMachineEntity(String ip, String userName, String password) {
        MachineEntity machineEntity = new MachineEntity();
        List<Disk> diskList = new LinkedList<>();
        List<DellCpuDTO> cpuDTOS = new LinkedList<>();
        List<DellMemoryDTO> memoryDTOS = new LinkedList<>();
        List<NetworkCard> networkCards = new LinkedList<>();
        long now = System.currentTimeMillis();
        if (login(ip, userName, password)) {
            try {
                Map headers = headersMap.get(ip);
                String result = HttpFutureUtils.getHttps(String.format(overviewUrl, ip), headers);
                JAXBContext context = JAXBContext.newInstance(DELL.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                DELL dell = (DELL) unmarshaller.unmarshal(new StringReader(result));

                JSONObject resObj = null;

                machineEntity.setSerialNo(dell.getSvcTag());
                machineEntity.setName("DELL " + dell.getSysDesc());
                machineEntity.setModel(dell.getSysDesc());
                machineEntity.setBmcIp(dell.getNetConfig().getIfConfig().getV4IPAddr());
                machineEntity.setBmcMac(dell.getNetConfig().getIfConfig().getMacAddr());
                machineEntity.setBrand("DELL");

                //cpu
                result = HttpFutureUtils.getHttps(String.format(getCPUUrl, ip), headers);
                resObj = JSON.parseObject(result);

                if (resObj.containsKey("Processor")) {
                    JSONObject cpus = resObj.getJSONObject("Processor");
                    Set<String> models = cpus.keySet();
                    models.forEach(m -> {
                        DellCpuDTO cpuDTO = gson.fromJson(cpus.getJSONObject(m).toJSONString(), DellCpuDTO.class);
                        cpuDTO.setSocket(m.substring(m.lastIndexOf(".") + 1));
                        cpuDTOS.add(cpuDTO);
                    });
                    if (cpuDTOS.size() > 0) {
                        machineEntity.setCpu(cpuDTOS.size());
                        machineEntity.setCpuFre(cpuDTOS.get(0).getCurrentSpeed() + "");
                        machineEntity.setCpuType(cpuDTOS.get(0).getBrand());
                        machineEntity.setCore(cpuDTOS.get(0).getCoreCount());
                        machineEntity.setThread(cpuDTOS.get(0).getHyperThreading().get(0).getEnabled());
                    }
                }


                //内存
                result = HttpFutureUtils.getHttps(String.format(getMemoryUrl, ip), headers);
                resObj = JSON.parseObject(result);
                if (resObj.containsKey("DIMM")) {
                    JSONObject memorys = resObj.getJSONObject("DIMM");
                    Set<String> models = memorys.keySet();
                    models.forEach(m -> {
                        DellMemoryDTO memoryDTO = gson.fromJson(memorys.getJSONObject(m).toJSONString(), DellMemoryDTO.class);
                        if (memoryDTO.getSize() == 0) {
                            return;
                        }
                        memoryDTOS.add(memoryDTO);
                    });

                    if (memoryDTOS.size() > 0) {
                        //内存存两份一份存extendInfo作为直接展示 另一份存另一个表
                        List<JSONObject> memoryDetails = new ArrayList<>();
                        memoryDTOS.forEach(memory -> {
                            JSONObject memoryDetail = new JSONObject();
                            memoryDetail.put("memorySize", new BigDecimal(memory.getSize()).divide(new BigDecimal("1024"), 0, BigDecimal.ROUND_HALF_UP).intValue());
                            memoryDetail.put("memoryType", convertMemoryType(memory.getType()).replace("-", ""));
                            machineEntity.setMemoryType(convertMemoryType(memory.getType()).replace("-", ""));
                            memoryDetails.add(memoryDetail);
                        });
                        HashMap<String, String> extendInfo = new HashMap<>();
                        extendInfo.put("memoryDetails", memoryDetails.toString());
                        machineEntity.setExtendInfo(extendInfo);
                        machineEntity.setMemory(memoryDTOS.stream().mapToInt(m -> m.getSize()).sum() / 1024);
                    }
                }

                result = HttpFutureUtils.getHttps(String.format(getAllVirtualDiskUrl, ip), headers);
                Set<String> vdiskIdSet = new HashSet<>();
                if (!StringUtils.isBlank(result)) {
                    JSONObject vdSummaryList = JSONObject.parseObject(result);
                    // 所有物理机虚拟磁盘ids
                    if (vdSummaryList.containsKey("VDisks")) {
                        JSONObject vdisks = vdSummaryList.getJSONObject("VDisks");
                        vdisks.keySet().forEach(i -> {
                            if (i.contains("|C|")) {
                                vdiskIdSet.add(i);
                            }
                        });
                    }
                }

                Map<Integer, Integer> slotRaidMap = new HashMap();
                if (vdiskIdSet != null && vdiskIdSet.size() > 0) {
                    for (String vId : vdiskIdSet) {
                        result = HttpFutureUtils.getHttps(String.format(getVirtualDiskUrl, ip, UriEncoder.encode(vId)), headers);
                        JSONObject vdiskDetailList = JSONObject.parseObject(result);
                        // 所有物理机磁盘ids
                        if (vdiskDetailList.containsKey("VDisks")) {
                            JSONObject vdiskDetail = vdiskDetailList.getJSONObject("VDisks");
                            vdiskDetail.keySet().forEach(i -> {
                                if (i.contains("|C|")) {
                                    String url = vdiskDetail.getJSONObject(i).getString("pdisks");
                                    String pdListUrl = "https://" + ip + url.split("\\?")[0] + "?" + UriEncoder.encode(url.split("\\?")[1]);
                                    String r = HttpFutureUtils.getHttps(pdListUrl, headers);
                                    JSONObject pdList = JSONObject.parseObject(r);
                                    if (pdList.containsKey("PDisks")) {
                                        pdList.getJSONObject("PDisks").keySet().forEach(p -> {
                                            slotRaidMap.put(pdList.getJSONObject("PDisks").getJSONObject(p).getInteger("slot"), vdiskDetail.getJSONObject(i).getInteger("raidlevel"));
                                        });
                                    }
                                }
                            });
                        }
                    }
                }

                //所有物理磁盘id
                result = HttpFutureUtils.getHttps(String.format(getAllDiskUrl, ip), headers);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject pdSummaryList = JSONObject.parseObject(result);
                    StringBuffer idSb = new StringBuffer();
                    // 所有物理机磁盘ids
                    if (pdSummaryList.containsKey("PDisks")) {
                        JSONObject pdisks = pdSummaryList.getJSONObject("PDisks");
                        for (String id : pdisks.keySet()) {
                            if (id.contains("|C|")) {
                                idSb.append(id).append(",");
                            }
                        }
                    }
                    if (idSb.length() > 0) {
                        idSb = new StringBuffer(idSb.substring(0, idSb.length() - 1));
                    }

                    //物理磁盘
                    result = HttpFutureUtils.getHttps(String.format(getDiskUrl, ip, UriEncoder.encode(idSb.toString())), headers);
                    JSONObject pdList = JSONObject.parseObject(result);
                    // 所有物理机磁盘ids
                    if (pdList.containsKey("PDisks")) {
                        JSONObject pdisks = pdList.getJSONObject("PDisks");
                        for (String pdName : pdisks.keySet()) {
                            DellPdDTO dellPdDTO = gson.fromJson(pdisks.getJSONObject(pdName).toJSONString(), DellPdDTO.class);
                            Disk disk = new Disk();
                            disk.setDrive(dellPdDTO.getSlot() + "");
                            disk.setManufactor(dellPdDTO.getManufacturer());
                            disk.setSn(dellPdDTO.getSerialNumber());
                            disk.setModel(dellPdDTO.getProductId());
                            disk.setSize(DiskUtils.getDiskManufactorValue((dellPdDTO.getSize() * 1.0 / (1024 * 1024 * 1024)) + ""));
                            disk.setSyncTime(now);
                            disk.setRaid(slotRaidMap.get(dellPdDTO.getSlot()) == null ? null : convertRaid(slotRaidMap.get(dellPdDTO.getSlot())));
                            disk.setControllerId(0);
                            disk.setType(dellPdDTO.getSasAddress() != null ? "SAS" : "SSD");
                            disk.setEnclosureId(dellPdDTO.getProtocol());
                            diskList.add(disk);
                        }
                    }
                }

                List<Activemac> xmlNetWorkCards = dell.getNdcmac().getActivemac();
                xmlNetWorkCards.forEach(n -> {
                    NetworkCard networkCard = new NetworkCard();
                    networkCard.setNumber(n.getNicname());
                    networkCard.setMac(n.getMacaddr());
                    networkCards.add(networkCard);
                });

                machineEntity.setDisks(diskList);
                machineEntity.setPmNetworkCards(networkCards);
            } catch (Exception e) {
                LogUtil.error("Dell IDrac7爬虫" + ip + "异常e：" + e + " machineEntity:" + JSON.toJSONString(machineEntity));
                headersMap.remove(ip);
                return null;
            }

            logout(ip);
        } else {
            return null;
        }
        setCPUandMemory(machineEntity, cpuDTOS, memoryDTOS);
        return machineEntity;
    }

    @Override
    public Integer getPowerMetric(String ip, String userName, String password) {
        if (login(ip, userName, password)) {
            try {
                Map headers = headersMap.get(ip);
                String result = HttpFutureUtils.getHttps(String.format(getPowerWattUrl, ip), headers);
                Pattern p = Pattern.compile("<reading>(\\d+)<\\/reading>");
                Matcher m = p.matcher(result);
                if (m.find()) {
                    return Integer.valueOf(m.group(1));
                }
                headersMap.remove(ip);
            } catch (Exception e) {
                headersMap.remove(ip);
                MetalPluginException.throwException(String.format("Dell:%s读取瞬时功耗值失败！", ip));
            }
        }
        return null;
    }

    private void setCPUandMemory(MachineEntity machineEntity, List<DellCpuDTO> cpuDTOS, List<DellMemoryDTO> memoryDTOS) {
        List<Cpu> cpus = new LinkedList<>();
        List<Memory> memories = new LinkedList<>();
        long now = System.currentTimeMillis();
        cpuDTOS.forEach(c -> {
            Cpu cpu = new Cpu();
            cpu.setProcName(c.getBrand());
            cpu.setProcNumCores(c.getCoreCount() + "");
            cpu.setProcNumCoresEnabled(c.getCoreCount() + "");
            cpu.setProcNumThreads(String.valueOf((c.getHyperThreading().get(0).getEnabled() == 1 ? 2 : 1) * c.getCoreCount()));
            cpu.setProcSocket(c.getSocket());
            cpu.setProcSpeed(c.getCurrentSpeed() + "");
            cpu.setProcStatus(convCPUStatus(c.getStatus()));
            cpu.setSyncTime(now);
            cpus.add(cpu);
        });

        memoryDTOS.forEach(c -> {
            Memory memory = new Memory();
//            memory.setMemCpuNum(String.valueOf(c.getMemCpuNum()));
            memory.setMemModFrequency(c.getSpeed() + "");
//            memory.setMemModMinVolt(String.valueOf(c.getMemModMinVolt() / 1000));
            memory.setMemModType(convertMemoryType(c.getType()).replace("-", ""));
//            memory.setMemModPartNum(c.getMemModPartNum());
            memory.setMemModSize(c.getSize() / 1024 + "");
            memory.setMemModNum(c.getName());
            memory.setSyncTime(now);
            memories.add(memory);
        });
        machineEntity.setPmCpus(cpus);
        machineEntity.setPmMemories(memories);
    }

    private String convertRaid(int type) {
        switch (type) {
            case 1:
                return "no_raid";
            case 2:
                return "RAID-0";
            case 4:
                return "RAID-1";
            case 64:
                return "RAID-5";
            case 128:
                return "RAID-6";
            case 2048:
                return "RAID-10";
            default:
                return "unknown";
        }
    }

    private String convertMemoryType(int type) {
        switch (type) {
            case 1:
                return "other";
            case 3:
                return "DRAM";
            case 4:
                return "EDRAM";
            case 5:
                return "VRAM";
            case 6:
                return "SRAM";
            case 7:
                return "RAM";
            case 8:
                return "ROM";
            case 9:
                return "Flash";
            case 10:
                return "EEPROM";
            case 11:
                return "FEPROM";
            case 12:
                return "EPROM";
            case 13:
                return "CDRAM";
            case 14:
                return "3DRAM";
            case 15:
                return "SDRAM";
            case 16:
                return "SGRAM";
            case 17:
                return "RDRAM";
            case 18:
                return "DDR";
            case 19:
                return "DDR-2";
            case 20:
                return "DDR-2-FB-DIMM";
            case 24:
                return "DDR-3";
            case 25:
                return "FBD2";
            default:
                return "info_not_available";
        }
    }

    private String convCPUStatus(int state) {
        switch (state) {
            case 2:
                return "status_ok";
            case 3:
                return "status_noncritical";
            case 4:
                return "status_critical";
            default:
                return "status_unknown";
        }
    }

    @XmlRootElement(name = "root")
    private static class DELL {

        private String sysDesc;
        private String svcTag;
        private NetConfig netConfig;
        private Ndcmac ndcmac;

        public Ndcmac getNdcmac() {
            return ndcmac;
        }

        public void setNdcmac(Ndcmac ndcmac) {
            this.ndcmac = ndcmac;
        }

        public String getSvcTag() {
            return svcTag;
        }

        public void setSvcTag(String svcTag) {
            this.svcTag = svcTag;
        }

        public NetConfig getNetConfig() {
            return netConfig;
        }

        public void setNetConfig(NetConfig netConfig) {
            this.netConfig = netConfig;
        }

        public String getSysDesc() {
            return sysDesc;
        }

        public void setSysDesc(String sysDesc) {
            this.sysDesc = sysDesc;
        }
    }

    @XmlRootElement(name = "netConfig")
    private static class NetConfig {
        private IfConfig ifConfig;

        public IfConfig getIfConfig() {
            return ifConfig;
        }

        public void setIfConfig(IfConfig ifConfig) {
            this.ifConfig = ifConfig;
        }
    }

    @XmlRootElement(name = "ifConfig")
    private static class IfConfig {
        private String macAddr;
        private String v4IPAddr;

        public String getV4IPAddr() {
            return v4IPAddr;
        }

        public void setV4IPAddr(String v4IPAddr) {
            this.v4IPAddr = v4IPAddr;
        }

        public String getMacAddr() {
            return macAddr;
        }

        public void setMacAddr(String macAddr) {
            this.macAddr = macAddr;
        }
    }

    @XmlRootElement(name = "ndcmac")
    private static class Ndcmac {
        private List<Activemac> activemac;

        public List<Activemac> getActivemac() {
            return activemac == null ? null : activemac.stream().filter(n -> StringUtils.isNotBlank(n.getNicname())).collect(Collectors.toList());
        }

        public void setActivemac(List<Activemac> activemac) {
            this.activemac = activemac;
        }
    }

    @XmlRootElement(name = "activemac")
    private static class Activemac {
        private String nicname;
        private String mactype;
        private String macaddr;

        public String getNicname() {
            return nicname;
        }

        public void setNicname(String nicname) {
            this.nicname = nicname;
        }

        public String getMactype() {
            return mactype;
        }

        public void setMactype(String mactype) {
            this.mactype = mactype;
        }

        public String getMacaddr() {
            return macaddr;
        }

        public void setMacaddr(String macaddr) {
            this.macaddr = macaddr;
        }
    }

    public static void main(String[] args) throws IOException, SAXException, DocumentException {
//        String ip = "10.132.47.215";
//        Header[] cookies = HttpFutureUtils.getHttpsReponseHeader(String.format(getCookieUrl, ip), null);
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Cookie", getCookie(cookies, "SET-COOKIE"));
//        Map request = new HashMap();
//        request.put("user", "root");
//        request.put("password", "Calong@2015");
//        String entityString = "user=root&password=Calong@2015";
//        HttpResponse response = HttpFutureUtils.postFormHttpsResponse(String.format(getSt1Url, ip), headers, entityString);
//        String XMLResponse = EntityUtils.toString(response.getEntity());
//        SAXReader saxReader = new SAXReader();
//        String forwardUrl = saxReader.read(new ByteArrayInputStream(XMLResponse.getBytes("UTF-8"))).getRootElement().element("forwardUrl").getText();
////
//        String pageST2 = HttpFutureUtils.getHttps(String.format(url, ip, forwardUrl), headers);
//
//        String tempArr[] = pageST2.split("\n");
//        String ST2 = null;
//        for (int i = 0; i < tempArr.length; i++) {
//            if (tempArr[i].split("=").length != 2) continue;
//            if (tempArr[i].split("=")[0].trim().equalsIgnoreCase("var TOKEN_VALUE")) {
//                ST2 = tempArr[i].split("=")[1].trim();
//                break;
//            }
//        }
//        headers.put("ST2", ST2.replace("\"", "").replace(";", ""));
//        headers.put("X_SYSMGMT_OPTIMIZE", "true");

//        String test = "";
//        try {
//            JAXBContext context = JAXBContext.newInstance(DELL.class);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            DELL dell = (DELL) unmarshaller.unmarshal(new StringReader(test));
//            System.out.println(dell);
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//        System.out.println(HttpFutureUtils.getHttps("https://10.132.47.215/sysmgmt/2012/server/memory\"", headers));
//        System.out.printloadln(EntityUtils.toString(HttpFutureUtils.getHttps("https://10.132.47.215/sysmgmt/2012/server/memory", headers).getEntity()));
//        System.out.println(pageST2);

        List<Disk> diskList = new LinkedList<>();
        JSONObject pdSummaryList = JSONObject.parseObject("{\"PDisks\": {\n" +
                "    \"304|C|Disk.Bay.0:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|C|Disk.Bay.1:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|C|Disk.Bay.2:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|C|Disk.Bay.3:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|C|Disk.Bay.4:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|C|Disk.Bay.5:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|C|Disk.Bay.6:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|C|Disk.Bay.7:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|P|Disk.Bay.0:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|P|Disk.Bay.1:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|P|Disk.Bay.2:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|P|Disk.Bay.3:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|P|Disk.Bay.4:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|P|Disk.Bay.5:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|P|Disk.Bay.6:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {},\n" +
                "    \"304|P|Disk.Bay.7:Enclosure.Internal.0-1:RAID.Integrated.1-1\": {}\n" +
                "}}");
        StringBuffer idSb = new StringBuffer();
        // 所有物理机磁盘ids
        if (pdSummaryList.containsKey("PDisks")) {
            JSONObject pdisks = pdSummaryList.getJSONObject("PDisks");
            for (String id : pdisks.keySet()) {
                if (id.contains("|C|")) {
                    idSb.append(id).append(",");
                }
            }
        }
        if (idSb.length() > 0) {
            idSb = new StringBuffer(idSb.substring(0, idSb.length() - 1));
        }

        //物理磁盘
        JSONObject pdList = JSONObject.parseObject("");
        // 所有物理机磁盘ids
        if (pdList.containsKey("PDisks")) {
            JSONObject pdisks = pdList.getJSONObject("PDisks");
            for (String pdName : pdisks.keySet()) {
                DellPdDTO dellPdDTO = gson.fromJson(pdisks.getJSONObject(pdName).toJSONString(), DellPdDTO.class);
                Disk disk = new Disk();
                disk.setDrive(dellPdDTO.getSlot() + "");
                disk.setManufactor(dellPdDTO.getManufacturer());
                disk.setSn(dellPdDTO.getSerialNumber());
                disk.setModel(dellPdDTO.getProductId());
                disk.setSize(DiskUtils.getDiskManufactorValue((dellPdDTO.getSize() * 1.0 / (1024 * 1024 * 1024)) + ""));
                disk.setSyncTime(System.currentTimeMillis());
                disk.setControllerId(0);
                disk.setType(dellPdDTO.getSasAddress() != null ? "SAS" : "SSD");
                diskList.add(disk);
            }
        }

        System.out.println(diskList);
    }
}
