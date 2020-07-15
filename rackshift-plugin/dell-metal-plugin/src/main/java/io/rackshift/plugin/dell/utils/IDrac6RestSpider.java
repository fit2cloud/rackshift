package io.rackshift.plugin.dell.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.metal.sdk.MetalPluginException;
import io.rackshift.plugin.dell.model.DellCpuDTO;
import io.rackshift.plugin.dell.model.DellMemoryDTO;
import com.google.gson.Gson;
import io.rackshift.metal.sdk.model.*;
import io.rackshift.metal.sdk.util.*;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDrac6RestSpider implements IIDRACRestAPI {

    public static final String overviewUrl = "https://%s/data?get=pwState,sysDesc,sysRev,hostName,osName,osVersion,svcTag,expSvcCode,biosVer,fwVersion,LCCfwVersion,v4Enabled,v4IPAddr,v6Enabled,v6LinkLocal,v6Addr,v6SiteLocal,macAddr,batteries,fansRedundancy,fans,intrusion,psRedundancy,powerSupplies,rmvsRedundancy,removableStorage,temperatures,voltages,kvmEnabled";
    public static final String hardWareUrl = "https://%s/data?get=GetInv";
    public static final String hardWareConfigUrl = "https://%s/data?get=GetInvConfig";
    public static final String getCookieUrl = "https://%s/login.html";
    public static final String indexUrl = "https://%s/index.html";
    public static final String getSt1Url = "https://%s/data/login";
    public static final String logoutUrl = "https://%s/data/logout";
    public static final String getPowerWattUrl = "https://%s/data?get=powermonitordata,powergraphdata,psRedundancy,pwState,pbtEnabled,activePLPolicy,activePLimit,pwrBudgetVal,powerSupplies,pbMaxWatts,";

    public static ConcurrentHashMap<String, Map<String, String>> headersMap;
    public static Gson gson;
    public static DateTimeFormatter dateTimeFormatter;

    static {
        try {
            headersMap = new ConcurrentHashMap<>();
            gson = new Gson();
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String escapeStr(String string) {
        return (String) OutBandUtil.escapeStr(string);
    }

    public String handleGermanChar(String string) {
        return (String) OutBandUtil.handleGermanChar(string);
    }


    public boolean loginCurl(String ip, String userName, String password) {
        try {
            if (headersMap.get(ip) != null) {
                return true;
            }
            Result result = DellUtils.login(ip, escapeStr(userName), escapeStr(password));
            String cookie = result.getHeader().get("set-cookie");
            String XMLResponse = result.getResultStr();
            SAXReader saxReader = new SAXReader();
            Element responseElement = saxReader.read(new ByteArrayInputStream(XMLResponse.getBytes("UTF-8"))).getRootElement();
            if (responseElement.element("authResult") != null && "0".equalsIgnoreCase(responseElement.element("authResult").getText())) {
                Map header = new HashMap();
                header.put("Cookie", cookie);
                headersMap.put(ip, header);
                return true;
            }
        } catch (Exception e) {
            LogUtil.error(String.format("爬取Dell idrac6 %s硬件信息时，获取重定向地址失败！可能是达到了最大爬取次数！清空缓存！e:%s", ip, ExceptionUtils.getExceptionDetail(e)));
            logout(ip);
        }
        return false;
    }

    @Override
    public boolean login(String ip, String userName, String password) {
        return loginCurl(ip, userName, password);
//        CloseableHttpClient httpClient = null;
//        try {
//            if (headersMap.get(ip) != null) {
//                return true;
//            }
//            httpClient = HttpFutureUtils.getOneHttpClient();
//            HttpGet httpGet = new HttpGet(String.format(getCookieUrl, ip));
//
//            CloseableHttpResponse response = httpClient.execute(httpGet);
//            LogUtil.info(String.format("爬取Dell idrac6 %s登录接口返回内容：%s", ip, EntityUtils.toString(response.getEntity())));
//            Header[] cookies = response.getAllHeaders();
//            if (cookies != null) {
//                LogUtil.info(String.format("爬取Dell idrac6 %s登录接口返回所有Cookie：%s", ip, JSONObject.toJSONString(cookies)));
//            } else {
//                LogUtil.info(String.format("爬取Dell idrac6 %s登录接口返回所有Cookie：为空", ip));
//                return false;
//            }
//
//            Map<String, String> headers = new HashMap<>();
//            RequestUtils.idrac6SetHeaders(ip, cookies, headers);
//
//            String entityString = String.format("user=%s&password=%s", escapeStr(userName), escapeStr(password));
//
//            Map resultMap = HttpFutureUtils.postHttpsResponse(String.format(getSt1Url, ip), entityString, "application/x-www-form-urlencoded", null);
//
//            String XMLResponse = (String) resultMap.get("response");
//            LogUtil.info(String.format("爬取Dell idrac6 %s登录接口返回结果：%s", ip, XMLResponse));
//            SAXReader saxReader = new SAXReader();
//            Element responseElement = saxReader.read(new ByteArrayInputStream(XMLResponse.getBytes("UTF-8"))).getRootElement();
//            if (responseElement.element("authResult") != null && "0".equalsIgnoreCase(responseElement.element("authResult").getText())) {
//                RequestUtils.idrac6SetHeaders(ip, (Header[]) resultMap.get("headers"), headers);
//                RequestUtils.idrac6FormatHeaders(ip, headers);
//                headersMap.put(ip, headers);
//                return true;
//            }
//        } catch (Exception e) {
//            LogUtil.error(String.format("爬取Dell idrac6 %s硬件信息时，获取重定向地址失败！可能是达到了最大爬取次数！清空缓存！e:%s", ip, ExceptionUtils.getExceptionDetail(e)));
//            logout(ip);
//        } finally {
//            if (httpClient != null) {
//                try {
//                    httpClient.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return false;
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

    public void removeSession(String ip) {
        headersMap.remove(ip);
    }

    public static Result getInfo(String url, String cookie) {
        String[] cmds = {"curl", "-i", "-k", "-X", "GET", "-b", cookie, "-H", "Content-Type:application/x-www-form-urlencoded", url};
        Result result = CURLUtils.execCurl(cmds);
        return result;
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

                machineEntity.setSerialNo(dell.getSvcTag());
                machineEntity.setName("DELL " + dell.getSysDesc());
                machineEntity.setModel(dell.getSysDesc());
                machineEntity.setBmcIp(dell.getNetConfig().getIfConfig().getV4IPAddr());
                machineEntity.setBmcMac(dell.getNetConfig().getIfConfig().getMacAddr());
                machineEntity.setBrand("DELL");

                //硬件
                result = HttpFutureUtils.postHttps(String.format(hardWareUrl, ip), "", "application/x-www-form-urlencoded", headers);
                LogUtil.info(String.format("第%s次HttpClient尝试获取旧idrac6接口 ip:%s的硬件信息为：%s", 1, ip, result));
                result = getInfo(String.format(hardWareUrl, ip), String.valueOf(headers.get("Cookie"))).getResultStr();
                LogUtil.info(String.format("第%s次Curl尝试获取旧idrac6接口 ip:%s的硬件信息为：%s", 1, ip, result));
                if (StringUtils.isNotBlank(result) && !result.contains("HW_inv_Empty") && result.contains("<HWINVs>")) {
                    context = JAXBContext.newInstance(DELLHardWare.class);
                    unmarshaller = context.createUnmarshaller();
                    DELLHardWare dellHardWare = (DELLHardWare) unmarshaller.unmarshal(new StringReader(result));

                    if (dellHardWare.getHwinVs().getDcim_cpuViews() != null) {
                        dellHardWare.getHwinVs().getDcim_cpuViews().getCpuViews().forEach(m -> {
                            DellCpuDTO cpuDTO = new DellCpuDTO();
                            cpuDTO.setSocket(m.getInstanceID());
                            cpuDTO.setName(m.getModel());
                            cpuDTO.setCurrentSpeed((int) (Double.valueOf(m.getModel().split("@")[1].replace(" ", "").replace("GHz", "")) * 1000));
                            cpuDTO.setCoreCount(0);
                            cpuDTOS.add(cpuDTO);
                        });
                        if (cpuDTOS.size() > 0) {
                            machineEntity.setCpu(cpuDTOS.size());
                            machineEntity.setCpuFre(cpuDTOS.get(0).getCurrentSpeed() + "");
                            machineEntity.setCpuType(cpuDTOS.get(0).getBrand());
                            machineEntity.setCore(cpuDTOS.get(0).getCoreCount());
                        }
                    }

                    //内存
                    if (dellHardWare.getHwinVs().getDcim_memoryViews() != null) {
                        dellHardWare.getHwinVs().getDcim_memoryViews().getMemoryViews().forEach(m -> {
                            DellMemoryDTO memoryDTO = new DellMemoryDTO();
                            memoryDTO.setSize((Integer.valueOf(m.getSize().replace("MB", "").replace(" ", ""))));
                            memoryDTO.setSpeed(Integer.valueOf(m.getSpeed().replace("MHz", "").replace(" ", "")));
                            memoryDTO.setType(24);
                            memoryDTO.setName(m.getInstanceID());
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

                    //所有物理磁盘id
                    if (dellHardWare.getHwinVs().getDcim_physicalDiskViews() != null) {
                        dellHardWare.getHwinVs().getDcim_physicalDiskViews().getDiskViews().forEach(dellPdDTO -> {
                            Disk disk = new Disk();
                            disk.setDrive(dellPdDTO.getInstanceID().substring(0, dellPdDTO.getInstanceID().indexOf(":")));
                            disk.setManufactor(dellPdDTO.getManufacturer());
                            disk.setSn(dellPdDTO.getSerialNumber());
                            disk.setModel(dellPdDTO.getModel());
                            disk.setSize(DiskUtils.getDiskManufactorValue((Long.valueOf(dellPdDTO.getSizeInBytes().replace("Bytes", "").replace(" ", "")) * 1.0 / (1024 * 1024 * 1024)) + ""));
                            disk.setSyncTime(now);
                            disk.setControllerId(0);
                            disk.setType("SAS");
                            disk.setEnclosureId(0);
                            diskList.add(disk);
                        });
                    }

                    List<DCIM_NICView> nicViews = dellHardWare.getHwinVs().getDcim_nicViews().getNicViews();
                    nicViews.forEach(n -> {
                        NetworkCard networkCard = new NetworkCard();
                        networkCard.setNumber(n.getInstanceID());
                        networkCard.setId(n.getProductName());
                        networkCard.setMac(n.getCurrentMACAddress());
                        networkCards.add(networkCard);
                    });
                }
                machineEntity.setDisks(diskList);
                machineEntity.setPmNetworkCards(networkCards);
            } catch (Exception e) {
                LogUtil.error("Dell idrac6爬虫" + ip + "异常e：" + ExceptionUtils.getExceptionDetail(e) + " machineEntity:" + JSON.toJSONString(machineEntity));
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

    public void setCPUandMemory(MachineEntity machineEntity, List<DellCpuDTO> cpuDTOS, List<DellMemoryDTO> memoryDTOS) {
        List<Cpu> cpus = new LinkedList<>();
        List<Memory> memories = new LinkedList<>();
        long now = System.currentTimeMillis();
        cpuDTOS.forEach(c -> {
            Cpu cpu = new Cpu();
            cpu.setProcName(c.getBrand());
            cpu.setProcNumCores(c.getCoreCount() + "");
            cpu.setProcNumCoresEnabled(c.getCoreCount() + "");
            cpu.setProcNumThreads("0");
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

    public String convertMemoryType(int type) {
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

    public String convCPUStatus(int state) {
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
    public static class DELL {

        public String sysDesc;

        public String svcTag;

        public NetConfig netConfig;

        @XmlTransient
        public String getSysDesc() {
            return sysDesc;
        }

        public void setSysDesc(String sysDesc) {
            this.sysDesc = sysDesc;
        }

        @XmlTransient
        public String getSvcTag() {
            return svcTag;
        }

        public void setSvcTag(String svcTag) {
            this.svcTag = svcTag;
        }

        @XmlTransient
        public NetConfig getNetConfig() {
            return netConfig;
        }

        public void setNetConfig(NetConfig netConfig) {
            this.netConfig = netConfig;
        }
    }

    @XmlRootElement(name = "netConfig")
    public static class NetConfig {

        public IfConfig ifConfig;

        @XmlTransient
        public IfConfig getIfConfig() {
            return ifConfig;
        }

        public void setIfConfig(IfConfig ifConfig) {
            this.ifConfig = ifConfig;
        }
    }

    @XmlRootElement(name = "ifConfig")
    public static class IfConfig {

        public String macAddr;

        public String v4IPAddr;

        @XmlTransient
        public String getV4IPAddr() {
            return v4IPAddr;
        }

        public void setV4IPAddr(String v4IPAddr) {
            this.v4IPAddr = v4IPAddr;
        }

        @XmlTransient
        public String getMacAddr() {
            return macAddr;
        }

        public void setMacAddr(String macAddr) {
            this.macAddr = macAddr;
        }
    }

    @XmlRootElement(name = "root")
    public static class DELLHardWare {

        @XmlElement(name = "HWINVs")
        public HWINVs hwinVs;

        @XmlTransient
        public HWINVs getHwinVs() {
            return hwinVs;
        }

        public void setHwinVs(HWINVs hwinVs) {
            this.hwinVs = hwinVs;
        }
    }

    @XmlRootElement(name = "HWINVs")
    public static class HWINVs {

        @XmlElement(name = "DCIM_CPUViews")
        public DCIM_CPUViews dcim_cpuViews;

        @XmlElement(name = "DCIM_MemoryViews")
        public DCIM_MemoryViews dcim_memoryViews;

        @XmlElement(name = "DCIM_PhysicalDiskViews")
        public DCIM_PhysicalDiskViews dcim_physicalDiskViews;

        @XmlElement(name = "DCIM_NICViews")
        public DCIM_NICViews dcim_nicViews;

        @XmlTransient
        public DCIM_NICViews getDcim_nicViews() {
            return dcim_nicViews;
        }

        public void setDcim_nicViews(DCIM_NICViews dcim_nicViews) {
            this.dcim_nicViews = dcim_nicViews;
        }

        @XmlTransient
        public DCIM_PhysicalDiskViews getDcim_physicalDiskViews() {
            return dcim_physicalDiskViews;
        }

        public void setDcim_physicalDiskViews(DCIM_PhysicalDiskViews dcim_physicalDiskViews) {
            this.dcim_physicalDiskViews = dcim_physicalDiskViews;
        }

        @XmlTransient
        public DCIM_MemoryViews getDcim_memoryViews() {
            return dcim_memoryViews;
        }

        public void setDcim_memoryViews(DCIM_MemoryViews dcim_memoryViews) {
            this.dcim_memoryViews = dcim_memoryViews;
        }

        @XmlTransient
        public DCIM_CPUViews getDcim_cpuViews() {
            return dcim_cpuViews;
        }

        public void setDcim_cpuViews(DCIM_CPUViews dcim_cpuViews) {
            this.dcim_cpuViews = dcim_cpuViews;
        }
    }

    @XmlRootElement(name = "DCIM_CPUViews")
    public static class DCIM_CPUViews {
        @XmlElement(name = "DCIM_CPUView")
        public List<DCIM_CPUView> cpuViews;

        @XmlTransient
        public List<DCIM_CPUView> getCpuViews() {
            return cpuViews;
        }

        public void setCpuViews(List<DCIM_CPUView> cpuViews) {
            this.cpuViews = cpuViews;
        }
    }

    @XmlRootElement(name = "DCIM_NICViews")
    public static class DCIM_NICViews {
        @XmlElement(name = "DCIM_NICView")
        public List<DCIM_NICView> nicViews;

        @XmlTransient
        public List<DCIM_NICView> getNicViews() {
            return nicViews;
        }

        public void setNicViews(List<DCIM_NICView> nicViews) {
            this.nicViews = nicViews;
        }
    }

    @XmlRootElement(name = "DCIM_NICView")
    public static class DCIM_NICView {

        public String InstanceID;

        public String ProductName;

        public String PermanentiSCSIMACAddress;

        public String PermanentMACAddress;
        public String CurrentMACAddress;

        @XmlTransient
        public String getInstanceID() {
            return InstanceID;
        }

        public void setInstanceID(String instanceID) {
            InstanceID = instanceID;
        }

        @XmlTransient
        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        @XmlTransient
        public String getPermanentiSCSIMACAddress() {
            return PermanentiSCSIMACAddress;
        }

        public void setPermanentiSCSIMACAddress(String permanentiSCSIMACAddress) {
            PermanentiSCSIMACAddress = permanentiSCSIMACAddress;
        }

        @XmlTransient
        public String getPermanentMACAddress() {
            return PermanentMACAddress;
        }

        public void setPermanentMACAddress(String permanentMACAddress) {
            PermanentMACAddress = permanentMACAddress;
        }

        @XmlTransient
        public String getCurrentMACAddress() {
            return CurrentMACAddress;
        }

        public void setCurrentMACAddress(String currentMACAddress) {
            CurrentMACAddress = currentMACAddress;
        }
    }

    @XmlRootElement(name = "DCIM_PhysicalDiskViews")
    public static class DCIM_PhysicalDiskViews {
        @XmlElement(name = "DCIM_PhysicalDiskView")
        public List<DCIM_PhysicalDiskView> diskViews;

        @XmlTransient
        public List<DCIM_PhysicalDiskView> getDiskViews() {
            return diskViews;
        }

        public void setDiskViews(List<DCIM_PhysicalDiskView> diskViews) {
            this.diskViews = diskViews;
        }
    }

    @XmlRootElement(name = "DCIM_MemoryViews")
    public static class DCIM_MemoryViews {
        @XmlElement(name = "DCIM_MemoryView")
        public List<DCIM_MemoryView> memoryViews;

        @XmlTransient
        public List<DCIM_MemoryView> getMemoryViews() {
            return memoryViews;
        }

        public void setMemoryViews(List<DCIM_MemoryView> memoryViews) {
            this.memoryViews = memoryViews;
        }
    }

    @XmlRootElement(name = "DCIM_PhysicalDiskView")
    public static class DCIM_PhysicalDiskView {
        public String InstanceID;
        public String Model;
        public String Manufacturer;
        public String SizeInBytes;
        public String SerialNumber;

        @XmlTransient
        public String getModel() {
            return Model;
        }

        public void setModel(String model) {
            Model = model;
        }

        @XmlTransient
        public String getManufacturer() {
            return Manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            Manufacturer = manufacturer;
        }

        @XmlTransient
        public String getSizeInBytes() {
            return SizeInBytes;
        }

        public void setSizeInBytes(String sizeInBytes) {
            SizeInBytes = sizeInBytes;
        }

        @XmlTransient
        public String getSerialNumber() {
            return SerialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            SerialNumber = serialNumber;
        }

        @XmlTransient
        public String getInstanceID() {
            return InstanceID;
        }

        public void setInstanceID(String instanceID) {
            InstanceID = instanceID;
        }
    }

    @XmlRootElement(name = "DCIM_MemoryView")
    public static class DCIM_MemoryView {
        public String InstanceID;
        public String PartNumber;
        public String Model;
        public String Size;
        public String Speed;

        @XmlTransient
        public String getInstanceID() {
            return InstanceID;
        }

        public void setInstanceID(String instanceID) {
            InstanceID = instanceID;
        }

        @XmlTransient
        public String getPartNumber() {
            return PartNumber;
        }

        public void setPartNumber(String partNumber) {
            PartNumber = partNumber;
        }

        @XmlTransient
        public String getModel() {
            return Model;
        }

        public void setModel(String model) {
            Model = model;
        }

        @XmlTransient
        public String getSize() {
            return Size;
        }

        public void setSize(String size) {
            Size = size;
        }

        @XmlTransient
        public String getSpeed() {
            return Speed;
        }

        public void setSpeed(String speed) {
            Speed = speed;
        }
    }

    @XmlRootElement(name = "DCIM_CPUView")
    public static class DCIM_CPUView {
        public String InstanceID;
        public String Model;
        public String Manufacturer;

        @XmlTransient
        public String getInstanceID() {
            return InstanceID;
        }

        public void setInstanceID(String instanceID) {
            InstanceID = instanceID;
        }

        @XmlTransient
        public String getModel() {
            return Model;
        }

        public void setModel(String model) {
            Model = model;
        }

        @XmlTransient
        public String getManufacturer() {
            return Manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            Manufacturer = manufacturer;
        }
    }
}
