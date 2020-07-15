package io.rackshift.plugin.dell;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.metal.sdk.model.*;
import io.rackshift.metal.sdk.util.DiskUtils;
import io.rackshift.plugin.dell.model.DellCpuDTO;
import io.rackshift.plugin.dell.model.DellMemoryDTO;
import io.rackshift.plugin.dell.utils.IDrac6RestSpider;
import com.google.gson.Gson;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class IDrac6Test {

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

    public static void main(String[] args) throws JAXBException, IOException {

        MachineEntity machineEntity = new MachineEntity();
        List<Disk> diskList = new LinkedList<>();
        List<DellCpuDTO> cpuDTOS = new LinkedList<>();
        List<DellMemoryDTO> memoryDTOS = new LinkedList<>();
        List<NetworkCard> networkCards = new LinkedList<>();
        long now = System.currentTimeMillis();
        String result = readFile("idrac6newoverview.txt");
        JAXBContext context = JAXBContext.newInstance(IDrac6RestSpider.DELL.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        IDrac6RestSpider.DELL dell = (IDrac6RestSpider.DELL) unmarshaller.unmarshal(new StringReader(result));

        machineEntity.setSerialNo(dell.getSvcTag());
        machineEntity.setName("DELL " + dell.getSysDesc());
        machineEntity.setModel(dell.getSysDesc());
        machineEntity.setBmcIp(dell.getNetConfig().getIfConfig().getV4IPAddr());
        machineEntity.setBmcMac(dell.getNetConfig().getIfConfig().getMacAddr());
        machineEntity.setBrand("DELL");

        //硬件
        result = readFile("idrac6new2hardware.txt");
        context = JAXBContext.newInstance(IDrac6RestSpider.DELLHardWare.class);
        unmarshaller = context.createUnmarshaller();
        IDrac6RestSpider.DELLHardWare dellHardWare = (IDrac6RestSpider.DELLHardWare) unmarshaller.unmarshal(new StringReader(result));

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

        List<IDrac6RestSpider.DCIM_NICView> nicViews = dellHardWare.getHwinVs().getDcim_nicViews().getNicViews();
        nicViews.forEach(n -> {
            NetworkCard networkCard = new NetworkCard();
            networkCard.setNumber(n.getInstanceID());
            networkCard.setId(n.getProductName());
            networkCard.setMac(n.getCurrentMACAddress());
            networkCards.add(networkCard);
        });

        machineEntity.setDisks(diskList);
        machineEntity.setPmNetworkCards(networkCards);
        setCPUandMemory(machineEntity, cpuDTOS, memoryDTOS);
        System.out.println(new Gson().toJson(machineEntity));
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
