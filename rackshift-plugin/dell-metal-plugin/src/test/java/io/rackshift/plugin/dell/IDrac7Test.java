package io.rackshift.plugin.dell;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.metal.sdk.model.*;
import io.rackshift.metal.sdk.util.DiskUtils;
import io.rackshift.plugin.dell.model.DellCpuDTO;
import io.rackshift.plugin.dell.model.DellMemoryDTO;
import io.rackshift.plugin.dell.model.idrac7.DellIDrac7CpuDTO;
import io.rackshift.plugin.dell.model.idrac7.DellIDrac7DiskDTO;
import io.rackshift.plugin.dell.model.idrac7.DellIDrac7MemoryDTO;
import io.rackshift.plugin.dell.utils.IDrac6RestSpider;
import io.rackshift.plugin.dell.utils.IDrac7HWUtils;
import io.rackshift.plugin.dell.utils.IDrac8RestSpider;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class IDrac7Test {
    static String readFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(IDrac6Test.class.getResourceAsStream("/" + fileName)));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    @XmlRootElement(name = "root")
    public static class DELLHardWare {

        @XmlElement(name = "DCIM_CPUViews")
        public IDrac6RestSpider.DCIM_CPUViews dcim_cpuViews;

        @XmlElement(name = "DCIM_MemoryViews")
        public IDrac6RestSpider.DCIM_MemoryViews dcim_memoryViews;

        @XmlElement(name = "DCIM_PhysicalDiskViews")
        public IDrac6RestSpider.DCIM_PhysicalDiskViews dcim_physicalDiskViews;

        @XmlElement(name = "DCIM_NICViews")
        public IDrac6RestSpider.DCIM_NICViews dcim_nicViews;

        @XmlTransient
        public IDrac6RestSpider.DCIM_NICViews getDcim_nicViews() {
            return dcim_nicViews;
        }

        public void setDcim_nicViews(IDrac6RestSpider.DCIM_NICViews dcim_nicViews) {
            this.dcim_nicViews = dcim_nicViews;
        }

        @XmlTransient
        public IDrac6RestSpider.DCIM_PhysicalDiskViews getDcim_physicalDiskViews() {
            return dcim_physicalDiskViews;
        }

        public void setDcim_physicalDiskViews(IDrac6RestSpider.DCIM_PhysicalDiskViews dcim_physicalDiskViews) {
            this.dcim_physicalDiskViews = dcim_physicalDiskViews;
        }

        @XmlTransient
        public IDrac6RestSpider.DCIM_MemoryViews getDcim_memoryViews() {
            return dcim_memoryViews;
        }

        public void setDcim_memoryViews(IDrac6RestSpider.DCIM_MemoryViews dcim_memoryViews) {
            this.dcim_memoryViews = dcim_memoryViews;
        }

        @XmlTransient
        public IDrac6RestSpider.DCIM_CPUViews getDcim_cpuViews() {
            return dcim_cpuViews;
        }

        public void setDcim_cpuViews(IDrac6RestSpider.DCIM_CPUViews dcim_cpuViews) {
            this.dcim_cpuViews = dcim_cpuViews;
        }
    }

    @XmlRootElement(name = "DCIM_CPUViews")
    public static class DCIM_CPUViews {
        @XmlElement(name = "DCIM_CPUView")
        public List<IDrac6RestSpider.DCIM_CPUView> cpuViews;

        @XmlTransient
        public List<IDrac6RestSpider.DCIM_CPUView> getCpuViews() {
            return cpuViews;
        }

        public void setCpuViews(List<IDrac6RestSpider.DCIM_CPUView> cpuViews) {
            this.cpuViews = cpuViews;
        }
    }

    @XmlRootElement(name = "DCIM_NICViews")
    public static class DCIM_NICViews {
        @XmlElement(name = "DCIM_NICView")
        public List<IDrac6RestSpider.DCIM_NICView> nicViews;

        @XmlTransient
        public List<IDrac6RestSpider.DCIM_NICView> getNicViews() {
            return nicViews;
        }

        public void setNicViews(List<IDrac6RestSpider.DCIM_NICView> nicViews) {
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
        public List<IDrac6RestSpider.DCIM_PhysicalDiskView> diskViews;

        @XmlTransient
        public List<IDrac6RestSpider.DCIM_PhysicalDiskView> getDiskViews() {
            return diskViews;
        }

        public void setDiskViews(List<IDrac6RestSpider.DCIM_PhysicalDiskView> diskViews) {
            this.diskViews = diskViews;
        }
    }

    @XmlRootElement(name = "DCIM_MemoryViews")
    public static class DCIM_MemoryViews {
        @XmlElement(name = "DCIM_MemoryView")
        public List<IDrac6RestSpider.DCIM_MemoryView> memoryViews;

        @XmlTransient
        public List<IDrac6RestSpider.DCIM_MemoryView> getMemoryViews() {
            return memoryViews;
        }

        public void setMemoryViews(List<IDrac6RestSpider.DCIM_MemoryView> memoryViews) {
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

    static <T> List<T> wrapObject(Document doc, String attributeValue, Class<T> tClass) {

        List<Element> elements = (List<Element>) doc.getRootElement().getChildren().stream().filter(c -> ((Element) c).getAttribute("Classname").getValue().equalsIgnoreCase(attributeValue)).collect(Collectors.toList());
        List<T> resLst = new LinkedList<>();
        elements.forEach(e -> {
            try {
                T obj = tClass.newInstance();
                Arrays.stream(tClass.getDeclaredFields()).forEach(f -> {
                    try {
                        f.setAccessible(true);
                        f.set(obj, ((List<Element>) e.getContent().stream().filter(e1 -> e1 instanceof Element).collect(Collectors.toList())).stream().filter(p -> p.getAttribute("NAME").getValue().equalsIgnoreCase(f.getName())).findFirst().get().getContent(3).getValue().trim());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
                resLst.add(obj);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });
        return resLst;
    }

    public static void main(String[] args) throws JAXBException, IOException, TransformerException, ParserConfigurationException, SAXException, JDOMException {

        MachineEntity machineEntity = new MachineEntity();
        tryIDrac7API("", machineEntity);
        int diskSize = machineEntity.getDisks() == null ? 0 : machineEntity.getDisks().stream().mapToInt(d -> Integer.parseInt(d.getSize().replace("GB", "").replace("TB", "").replace(" ", ""))).sum();
        machineEntity.setDisk(diskSize);
        System.out.println(JSONObject.toJSONString(machineEntity));
    }


    /**
     * 一部分idrac7 登录退出 接口都是idrac8
     *
     * @param ip
     * @param machineEntity
     */
    private static boolean tryIDrac7API(String ip, MachineEntity machineEntity) throws JAXBException, JDOMException, IOException {
        String getModelUrl = "https://%s/data?get=sysDesc,biosVer,svcTag,nodeId,expSvcCode,osVersion,sysRev,LCCfwVersion,iDSDMVersion,recoveryAction,initCountdown,presentCountdown,datetime,fwVersion,fwUpdated,hwVersion,macAddr,dnsDomain,v4Enabled,v4IPAddr,v4Gateway,v4NetMask,v4DHCPEnabled,v4DHCPServers,v4DNS1,v4DNS2,v6Enabled,v6Addr,v6Gateway,v6DHCPEnabled,v6LinkLocal,v6Prefix,v6SiteLocal,v6SiteLocal3,v6SiteLocal4,v6SiteLocal5,v6SiteLocal6,v6SiteLocal7,v6SiteLocal8,v6SiteLocal9,v6SiteLocal10,v6SiteLocal11,v6SiteLocal12,v6SiteLocal13,v6SiteLocal14,v6SiteLocal15,racName,v6DHCPServers,v6DNS1,v6DNS2,devLocInfo,prodClassName,ndcmac,";
        String getHWUrl = "https://%s/sysmgmt/2012/server/inventory/hardware";
        long now = System.currentTimeMillis();
        List<Disk> diskList = new LinkedList<>();
        List<Cpu> cpuDTOS = new LinkedList<>();
        List<Memory> memoryDTOS = new LinkedList<>();
        List<NetworkCard> networkCards = new LinkedList<>();
        String result = readFile("idrac7overview.txt");
        if (StringUtils.isNotBlank(result)) {
            JAXBContext context = JAXBContext.newInstance(IDrac8RestSpider.DELL.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            IDrac8RestSpider.DELL dell = (IDrac8RestSpider.DELL) unmarshaller.unmarshal(new StringReader(result));
            //型号
            machineEntity.setSerialNo(dell.getSvcTag());
            machineEntity.setName("DELL " + dell.getSysDesc());
            machineEntity.setModel(dell.getSysDesc());
            machineEntity.setBmcIp(dell.getNetConfig().getIfConfig().getV4IPAddr());
            machineEntity.setBmcMac(dell.getNetConfig().getIfConfig().getMacAddr());
            machineEntity.setBrand("DELL");

            List<IDrac8RestSpider.Activemac> xmlNetWorkCards = dell.getNdcmac() != null ? dell.getNdcmac().getActivemac() : null;

            //网卡
            Optional.ofNullable(xmlNetWorkCards).ifPresent(x -> x.forEach(n -> {
                NetworkCard networkCard = new NetworkCard();
                networkCard.setNumber(n.getNicname());
                networkCard.setMac(n.getMacaddr());
                networkCards.add(networkCard);
            }));
            machineEntity.setPmNetworkCards(networkCards);
        }

        result = readFile("idrac7hw.xml");
        if (StringUtils.isNotBlank(result)) {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document doc = saxBuilder.build(new ByteArrayInputStream(result.getBytes()));
            List<DellIDrac7CpuDTO> dellIDrac7CpuDTOS = IDrac7HWUtils.wrapObject(doc, "DCIM_CPUView", DellIDrac7CpuDTO.class);
            List<DellIDrac7MemoryDTO> dellIDrac7MemoryDTOS = IDrac7HWUtils.wrapObject(doc, "DCIM_MemoryView", DellIDrac7MemoryDTO.class);
            List<DellIDrac7DiskDTO> dellIDrac7DiskDTOS = IDrac7HWUtils.wrapObject(doc, "DCIM_PhysicalDiskView", DellIDrac7DiskDTO.class);

            //cpu
            dellIDrac7CpuDTOS.forEach(c -> {
                Cpu cpu = new Cpu();
                cpu.setProcName(c.getModel());
                cpu.setProcNumCores(c.getNumberOfEnabledCores() != null ? c.getNumberOfEnabledCores() : "1");
                cpu.setProcNumCoresEnabled(c.getNumberOfEnabledCores() != null ? c.getNumberOfEnabledCores() : "1");
                cpu.setProcNumThreads(c.getNumberOfEnabledThreads());
                cpu.setProcSocket(c.getFQDD());
                cpu.setProcSpeed(c.getCurrentClockSpeed().replace("MHz", "").replace(" ", ""));
                cpu.setProcStatus("1");
                cpu.setSyncTime(now);
                cpuDTOS.add(cpu);
            });

            //内存
            dellIDrac7MemoryDTOS.forEach(c -> {
                Memory memory = new Memory();
                memory.setMemModFrequency(c.getSpeed() + "");
                memory.setMemModType(c.getMemoryType() != null ? c.getMemoryType().replace("-", "") : "unknown");
                memory.setMemModPartNum(c.getPartNumber());
                memory.setMemModSize((c.getSize() != null ? Integer.valueOf(c.getSize().replace("MB", "").replace(" ", "")) / 1024 : 0) + "");
                memory.setMemModNum(c.getFQDD());
                memory.setSyncTime(now);
                memoryDTOS.add(memory);
            });

            //磁盘
            dellIDrac7DiskDTOS.forEach(d -> {
                Disk disk = new Disk();
                disk.setDrive(d.getSlot());
                disk.setSn(d.getSerialNumber());
                disk.setModel(d.getModel());
                disk.setSize(DiskUtils.getDiskManufactorValue(Long.parseLong(d.getSizeInBytes().replace("Bytes", "").replace(" ", "")) * 1.0 / (1024 * 1024 * 1024) + ""));
                disk.setControllerId(0);
                disk.setManufactor(d.getManufacturer());
                disk.setSyncTime(now);
                disk.setType(d.getSASAddress() != null ? "SAS" : "SSD");
                disk.setEnclosureId(0);
                diskList.add(disk);
            });

            if (cpuDTOS.size() > 0) {
                machineEntity.setCpu(cpuDTOS.size());
                machineEntity.setCpuFre(cpuDTOS.get(0).getProcSpeed() != null ? cpuDTOS.get(0).getProcSpeed() + "" : "unknown");
                machineEntity.setCpuType(cpuDTOS.get(0).getProcName());
                machineEntity.setCore(cpuDTOS.get(0).getProcNumCores() != null ? Integer.parseInt(cpuDTOS.get(0).getProcNumCores()) : 1);
                machineEntity.setThread(cpuDTOS.get(0).getProcNumThreads() != null ? Integer.valueOf(cpuDTOS.get(0).getProcNumThreads()) : 1);
            }

            if (memoryDTOS.size() > 0) {
                //内存存两份一份存extendInfo作为直接展示 另一份存另一个表
                List<JSONObject> memoryDetails = new ArrayList<>();
                memoryDTOS.forEach(memory -> {
                    JSONObject memoryDetail = new JSONObject();
                    memoryDetail.put("memorySize", memory.getMemModSize());
                    memoryDetail.put("memoryType", memory.getMemModType());
                    machineEntity.setMemoryType(memory.getMemModType());
                    memoryDetails.add(memoryDetail);
                });
                HashMap<String, String> extendInfo = new HashMap<>();
                extendInfo.put("memoryDetails", memoryDetails.toString());
                machineEntity.setExtendInfo(extendInfo);
                machineEntity.setMemory(memoryDTOS.stream().mapToInt(m -> Integer.parseInt(m.getMemModSize())).sum());
            }

            machineEntity.setPmCpus(cpuDTOS);
            machineEntity.setPmMemories(memoryDTOS);
            machineEntity.setDisks(diskList);
        }
        //退出
        return true;
    }

    public static void setCPUandMemory(MachineEntity machineEntity, List<DellCpuDTO> cpuDTOS, List<DellMemoryDTO> memoryDTOS) {
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
//            cpu.setProcStatus(convCPUStatus(c.getStatus()));
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

    public static String convertMemoryType(int type) {
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
}
