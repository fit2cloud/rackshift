package io.rackshift.plugin.dell.utils;

import io.rackshift.metal.sdk.MetalPluginException;
import io.rackshift.metal.sdk.model.*;
import io.rackshift.metal.sdk.util.DiskUtils;
import io.rackshift.metal.sdk.util.SnmpWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DellSnmpHelper {
    private String ip;
    private String community;
    private int port;
    private SnmpWorker snmpWorker;

    private static final String CPUName = "1.3.6.1.4.1.674.10892.5.4.1100.30.1.23.1";
    private static final String CPUCore = "1.3.6.1.4.1.674.10892.5.4.1100.30.1.17.1";
    private static final String CPUThreadCount = "1.3.6.1.4.1.674.10892.5.4.1100.30.1.19.1";
    private static final String CPUFre = "1.3.6.1.4.1.674.10892.5.4.1100.30.1.12.1";
    private static final String MemTotalVolum = "1.3.6.1.4.1.674.10892.5.4.1100.50.1.14.1";
    private static final String phyical_driver = "1.3.6.1.4.1.674.10892.5.5.1.20.130.4.1.11";
    private static Logger logger = LoggerFactory.getLogger(DellSnmpHelper.class);

    public DellSnmpHelper(String ip, String community, int port) {
        this.ip = ip;
        this.community = community;
        this.port = port;
        try {
            this.snmpWorker = new SnmpWorker(ip, community, port);
        } catch (IOException e) {
            MetalPluginException.throwException(String.format("Dell:%s,snmp协议连接失败！", ip));
        }
    }

    private String getMachineName() throws MetalPluginException {
        try {
            Map<String, String> dataMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.1.3.12");
            return dataMap == null ? "DELL" : "DELL " + dataMap.entrySet().stream().findFirst().get().getValue();
        } catch (Exception e) {
        }
        return "DELL";
    }

    /**
     * 获取内存大小时要确定单位，管理中心在格式化的时候将值除以1024，此处得到的是GB，所以返回值需要乘以1024
     *
     * @return
     */
    private long getTotalMemory() {

        try {
            long memorySize = 0L;
            Map<String, String> walk = snmpWorker.walk(MemTotalVolum);
            for (Map.Entry<String, String> entry : walk.entrySet()) {
                if (entry.getKey() != null) {
                    memorySize = memorySize + Long.parseLong(entry.getValue());
                }
            }
            return memorySize / (1024 * 1024);
        } catch (Exception e) {
            logger.info(getLogMessage("获取内存失败"), e);
            return 0;
        }
    }

    private long getTotalStorage() {
        try {
            long diskSize = 0L;
            Map<String, String> walk = snmpWorker.walk(phyical_driver);
            for (Map.Entry<String, String> entry : walk.entrySet()) {
                if (entry.getKey() != null) {
                    diskSize = diskSize + Long.parseLong(entry.getValue());
                }
            }
            return diskSize / 1024;
        } catch (Exception e) {
            logger.info(getLogMessage("获取磁盘失败"), e);
            return 1024;
        }
    }

    private List<Cpu> getCpus() throws MetalPluginException {
        List<Cpu> cpus = new LinkedList<>();

        Map<String, String> nameMap = snmpWorker.walk(CPUName);
        Map<String, String> coreMap = snmpWorker.walk(CPUCore);
        Map<String, String> threadMap = snmpWorker.walk(CPUThreadCount);
        Map<String, String> freMap = snmpWorker.walk(CPUFre);
        Map<String, String> socketMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.4.1100.30.1.26.1");
        int i = 0;
        long now = System.currentTimeMillis();
        for (Map.Entry<String, String> entry : nameMap.entrySet()) {
            if (entry.getKey() != null) {
                Cpu cpuEntity = new Cpu();
                cpuEntity.setProcName(entry.getValue());
                cpuEntity.setProcNumCores(getValue(coreMap, i));
                cpuEntity.setProcNumThreads(getValue(threadMap, i));
                cpuEntity.setProcSpeed(getValue(freMap, i));
                cpuEntity.setSyncTime(now);
                cpuEntity.setProcSocket(getValue(socketMap, i).replace("CPU.Socket.", ""));
                cpus.add(cpuEntity);
            }
            i++;
        }
        return cpus;
    }

    public String getValue(Map<String, String> map, int i) {
        int index = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (i == index) {
                return entry.getValue();
            }
            index++;
        }
        return null;
    }

    public String getLogMessage(String physicalMachine) {
        return null;
    }

    public MachineEntity getMachineEntry() throws MetalPluginException {
        try {
            MachineEntity entry = new MachineEntity();
            entry.setIp(ip);
            String name = getMachineName();
            entry.setName(name);
            entry.setSerialNo(getSn());
            //内存
            entry.setMemory(getTotalMemory());
            //磁盘
            entry.setDisk(Long.parseLong(DiskUtils.getDiskManufactorValue(getTotalStorage() + "").replace("GB", "").replace(" ", "")));
            // 获取cpu相关
            List<Cpu> cpus = getCpus();
            entry.setPmCpus(cpus);
            List<Memory> memories = getMemorys();
            entry.setPmMemories(memories);
            List<Disk> disks = getDisks();
            entry.setDisks(disks);
            List<NetworkCard> networkCards = getNetworkCards();
            entry.setPmNetworkCards(networkCards);
            entry.setCpu(cpus.size());
            entry.setCore(Integer.valueOf(cpus.get(0).getProcNumCores()));
            entry.setCpuFre(cpus.get(0).getProcSpeed());
            entry.setCpuType(cpus.get(0).getProcName());
            return entry;
        } catch (Exception e) {
            MetalPluginException.throwException(String.format("获取Dell%s信息失败！", ip));
            return null;
        }
    }

    public List<Disk> getDisks() {
        List<Disk> disks = new LinkedList<>();
        try {
            Map<String, String> sizeMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.5.1.20.130.4.1.11");
            Map<String, String> snMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.5.1.20.130.4.1.7");
            Map<String, String> slotMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.5.1.20.130.4.1.2");
            Map<String, String> manufaMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.5.1.20.130.4.1.3");
            Map<String, String> modelMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.5.1.20.130.4.1.27");
            int i = 0;
            long now = System.currentTimeMillis();
            for (Map.Entry<String, String> entry : sizeMap.entrySet()) {
                if (entry.getKey() != null) {
                    Disk d = new Disk();
                    d.setSyncTime(now);
                    d.setType("SAS");
                    d.setControllerId(0);
                    d.setSize(DiskUtils.getDiskManufactorValue(Integer.valueOf(entry.getValue()) / 1024 + ""));
                    d.setSn(getValue(snMap, i));
                    d.setManufactor(getValue(manufaMap, i));
                    d.setModel(getValue(modelMap, i));
                    d.setDrive(getValue(slotMap, i).substring(getValue(slotMap, i).lastIndexOf(":") + 1));
                    d.setEnclosureId(0);
                    disks.add(d);
                }
                i++;
            }
            return disks;
        } catch (Exception e) {
            logger.info(getLogMessage("获取内存失败"), e);
            return disks;
        }
    }

    private List<NetworkCard> getNetworkCards() {
        List<NetworkCard> cards = new LinkedList<>();
        try {
            Map<String, String> cardMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.4.1100.90.1.15");
            int i = 0;
            long now = System.currentTimeMillis();
            for (Map.Entry<String, String> entry : cardMap.entrySet()) {
                if (entry.getKey() != null) {
                    NetworkCard d = new NetworkCard();
                    d.setSynTime(now);
                    d.setNumber("eth" + i);
                    d.setMac(entry.getValue());
                    cards.add(d);
                }
                i++;
            }
            return cards;
        } catch (Exception e) {
            logger.info(getLogMessage("获取网卡失败"), e);
            return cards;
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

    private List<Memory> getMemorys() {
        List<Memory> memories = new LinkedList<>();
        try {
            Map<String, String> sizeMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.4.1100.50.1.14.1");
            Map<String, String> snMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.4.1100.50.1.23.1");
            Map<String, String> speedMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.4.1100.50.1.15.1");
            Map<String, String> typeMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.4.1100.50.1.7");
            Map<String, String> slotMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.4.1100.50.1.26.1");
            Map<String, String> modelMap = snmpWorker.walk("1.3.6.1.4.1.674.10892.5.4.1100.50.1.22.1");
            int i = 0;
            long now = System.currentTimeMillis();
            for (Map.Entry<String, String> entry : sizeMap.entrySet()) {
                if (entry.getKey() != null) {
                    Memory pmMemory = new Memory();
                    pmMemory.setMemModSize((Integer.valueOf(entry.getValue()) / 1024 / 1024) + "");
                    pmMemory.setSn(getValue(snMap, i));
                    pmMemory.setMemModType(convertMemoryType(Integer.valueOf(getValue(typeMap, i))).replace("-", ""));
                    pmMemory.setMemModFrequency(getValue(speedMap, i));
                    pmMemory.setMemCpuNum(getValue(slotMap, i));
                    pmMemory.setMemModPartNum(getValue(modelMap, i));
                    pmMemory.setSyncTime(now);
                    memories.add(pmMemory);
                }
                i++;
            }
            return memories;
        } catch (Exception e) {
            logger.info(getLogMessage("获取内存失败"), e);
            return memories;
        }
    }

    private String getSn() throws IOException {
        return snmpWorker.getAsString("1.3.6.1.4.1.674.10892.5.4.300.10.1.11.1");
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new SnmpWorker("10.132.46.250", "public", 161).getAsString("1.3.6.1.2.1.1.5.0"));
    }
}
