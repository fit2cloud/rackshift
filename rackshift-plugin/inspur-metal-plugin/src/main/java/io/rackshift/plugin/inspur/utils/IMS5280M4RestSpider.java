package io.rackshift.plugin.inspur.utils;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.metal.sdk.model.*;
import io.rackshift.metal.sdk.model.request.IPMIRequest;
import io.rackshift.metal.sdk.util.*;
import io.rackshift.plugin.inspur.model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class IMS5280M4RestSpider extends AbstractIMSRestApi {


    private static final String overviewUrl = "http://%s/rpc/getfruinfo.asp";
    private static final String logoutUrl = "http://%s/rpc/WEBSES/logout.asp";
    private static final String getCookieUrl = "http://%s/rpc/WEBSES/create.asp";
    private static final String getBMCUrl = "http://%s/rpc/getalllancfg.asp";

    private static final String getCPUUrl = "http://%s/rpc/getcpuinfo.asp";
    private static final String getMemoryUrl = "http://%s/rpc/getmeminfo.asp";
    //    private static final String getDiskUrl = "http://%s/rpc/WEBSES/create.asp";
    private static final String getNetWorkUrl = "http://%s/rpc/getmacinfo.asp";

    @Override
    public boolean login(String ip, String userName, String password) {
        try {
            if (headerMap.get(ip) != null) {
                return true;
            }
            String result = HttpFutureUtils.postHttps(String.format(getCookieUrl, ip), String.format("WEBVAR_USERNAME=%s&WEBVAR_PASSWORD=%s", userName, password), "application/x-www-form-urlencoded");
            JSONObject sessionObj = JSONObject.parseObject(getResponseJSONString(result)).getJSONArray("WEBVAR_STRUCTNAME_WEB_SESSION").getJSONObject(0);
            String token = sessionObj.getString("SESSION_COOKIE");
            String csrfToken = sessionObj.getString("CSRFTOKEN");
            Map<String, String> headers = new HashMap();
            headers.put("Cookie", "SessionCookie=" + token);
            //新版固件会检查这个token老版的可能不需要
            if (StringUtils.isNotBlank(csrfToken)) {
                headers.put("CSRFTOKEN", csrfToken);
            }
            headerMap.put(ip, headers);
            return true;
        } catch (Exception e) {
            LogUtil.error(String.format("爬取浪潮ip：{%s}时，获取sessionKey失败！", ip), ExceptionUtils.getExceptionDetail(e));
        }
        return false;
    }

    @Override
    public MachineEntity getMachineEntity(String ip, String userName, String password) {
        MachineEntity machineEntity = new MachineEntity();
        List<Disk> diskList = new LinkedList<>();
        List<InspurCpuDTO.WEBVARSTRUCTNAMEGETCPUINFOBean> cpuDTOS = new LinkedList<>();
        List<InspurMemoryDTO.WEBVARSTRUCTNAMEGETMEMINFOBean> memoryDTOS = new LinkedList<>();
        List<NetworkCard> networkCards = new LinkedList<>();

        if (login(ip, userName, password)) {
            try {
                Map headers = headerMap.get(ip);

                String result = getResponseJSONString(OkHttpUtils.getHttps(String.format(overviewUrl, ip), headers));
                LogUtil.error(String.format("爬取浪潮ip：{%s}时，获取overviewUrl:{%s}", ip, result));
                InspurFruDTO inspurFruDTO = gson.fromJson(result, InspurFruDTO.class);
                InspurFruDTO.WEBVARSTRUCTNAMEHLGETALLFRUINFOBean fruBean = inspurFruDTO.getWEBVARSTRUCTNAMEHLGETALLFRUINFO().get(0);

                machineEntity.setSerialNo(fruBean.getPIProductSerialNum());
                machineEntity.setName("Inspur " + fruBean.getPIProductName());
                machineEntity.setModel(fruBean.getPIProductName());

                result = getResponseJSONString(OkHttpUtils.getHttps(String.format(getBMCUrl, ip), headers));
                LogUtil.error(String.format("爬取浪潮ip：{%s}时，getBMCUrl:{%s}", ip, result));
                InspurBMCDTO inspurBMCDTO = gson.fromJson(result, InspurBMCDTO.class);

                InspurBMCDTO.WEBVARSTRUCTNAMEGETALLNETWORKCFGBean bmcBean = inspurBMCDTO.getWEBVARSTRUCTNAMEGETALLNETWORKCFG().size() > 1 ? inspurBMCDTO.getWEBVARSTRUCTNAMEGETALLNETWORKCFG().get(1) : null;
                if (bmcBean != null) {
                    machineEntity.setBmcIp(bmcBean.getV4IPAddr());
                    machineEntity.setBmcMac(bmcBean.getMacAddress());
                }
                machineEntity.setBrand("Inspur");

                //cpu
                result = getResponseJSONString(OkHttpUtils.getHttps(String.format(getCPUUrl, ip), headers));
                LogUtil.error(String.format("爬取浪潮ip：{%s}时，getCPUUrl:{%s}", ip, result));
                InspurCpuDTO cpuDTO = gson.fromJson(result, InspurCpuDTO.class);

                cpuDTOS = cpuDTO.getWEBVARSTRUCTNAMEGETCPUINFO();

                if (cpuDTOS.size() > 0) {
                    LogUtil.error(String.format("爬取浪潮ip：{%s}时，CPU型号:{%s}", ip, cpuDTOS.get(0).getModel()));
                    if (StringUtils.isNotBlank(cpuDTOS.get(0).getModel())) {
                        machineEntity.setCpuFre((int) (Float.parseFloat(cpuDTOS.get(0).getModel().split("@")[1].replace("GHz", "")) * 1000) + "");
                        machineEntity.setCpuType(cpuDTOS.get(0).getModel());
                        machineEntity.setCore(cpuDTOS.get(0).getUsedCore());
                        machineEntity.setThread(2 * cpuDTOS.get(0).getUsedCore());
                    }
                }
                //内存
                result = getResponseJSONString(OkHttpUtils.getHttps(String.format(getMemoryUrl, ip), headers));
                LogUtil.error(String.format("爬取浪潮ip：{%s}时，getMemoryUrl:{%s}", ip, result));
                InspurMemoryDTO memoryDTO = gson.fromJson(result, InspurMemoryDTO.class);
                memoryDTOS = memoryDTO.getWEBVARSTRUCTNAMEGETMEMINFO();


                //内存存两份一份存extendInfo作为直接展示 另一份存另一个表
                List<JSONObject> memoryDetails = new ArrayList<>();
                memoryDTOS.forEach(memory -> {
                    if (memory.getCapacity() == 0) {
                        return;
                    }
                    JSONObject memoryDetail = new JSONObject();
                    memoryDetail.put("memorySize", memory.getCapacity());
                    memoryDetail.put("memoryType", memory.getSpeed() > 1866 ? "DDR4" : "DDR3");
                    machineEntity.setMemoryType(memory.getSpeed() > 1866 ? "DDR4" : "DDR3");
                    memoryDetails.add(memoryDetail);
                });
                HashMap<String, String> extendInfo = new HashMap<>();
                extendInfo.put("memoryDetails", memoryDetails.toString());
                machineEntity.setExtendInfo(extendInfo);
                machineEntity.setMemory(memoryDTOS.stream().mapToInt(m -> m.getCapacity()).sum());

                //网卡
                result = getResponseJSONString(OkHttpUtils.getHttps(String.format(getNetWorkUrl, ip), headers));
                LogUtil.error(String.format("爬取浪潮ip：{%s}时，getNetWorkUrl:{%s}", ip, result));
                InspurNicDTO inspurNicDTO = gson.fromJson(result, InspurNicDTO.class);
                inspurNicDTO.getWEBVARSTRUCTNAMEGETMACINFO().forEach(n -> {
                    if (n.getMACADDRESS() == null) {
                        return;
                    }
                    NetworkCard networkCard = new NetworkCard();
                    networkCard.setNumber("eth" + n.getIndex());
                    networkCard.setMac(n.getMACADDRESS());
                    networkCards.add(networkCard);
                });

                machineEntity.setDisks(diskList);
                machineEntity.setPmNetworkCards(networkCards);
            } catch (Exception e) {
                LogUtil.error(String.format("爬取浪潮ip:%s时发生异常", ip), ExceptionUtils.getExceptionDetail(e));
                headerMap.remove(ip);
                return null;
            }
            logout(ip, userName, password);
        }
        setCPUandMemory(machineEntity, cpuDTOS, memoryDTOS);
        return machineEntity;
    }

    @Override
    public Metric getMetric(String ip, String userName, String password) {
        if (login(ip, userName, password)) {
            try {
                Metric metric = new Metric();
                Map headers = headerMap.get(ip);

                String result = getResponseJSONString(OkHttpUtils.getHttps(String.format(overviewUrl, ip), headers));
                LogUtil.error(String.format("爬取浪潮ip：{%s}时，获取overviewUrl:{%s}", ip, result));
                IPMIRequest request = new IPMIRequest(ip, userName, password);
                IPMIUtils.exeCommand(request, "sdr");
                return metric;
            } catch (Exception e) {
                LogUtil.error(String.format("爬取浪潮ip:%s时发生异常", ip), ExceptionUtils.getExceptionDetail(e));
                headerMap.remove(ip);
                return null;
            }
        }
        return null;
    }

    private void setCPUandMemory(MachineEntity machineEntity, List<InspurCpuDTO.WEBVARSTRUCTNAMEGETCPUINFOBean> cpuDTOS, List<InspurMemoryDTO.WEBVARSTRUCTNAMEGETMEMINFOBean> memoryDTOS) {
        List<Cpu> cpus = new LinkedList<>();
        List<Memory> memories = new LinkedList<>();
        long now = System.currentTimeMillis();
        cpuDTOS.forEach(c -> {
            if (StringUtils.isBlank(c.getModel())) return;
            Cpu cpu = new Cpu();
            cpu.setProcName(c.getModel());
            cpu.setProcNumCores(c.getCoreNumber() + "");
            cpu.setProcNumCoresEnabled(c.getCoreNumber() + "");
            cpu.setProcNumThreads(2 + "");
            cpu.setProcSocket(c.getCPUID() + "");
            cpu.setProcSpeed((int) (Float.parseFloat(cpuDTOS.get(0).getModel().split("@")[1].replace("GHz", "")) * 1000) + "");
            cpu.setSyncTime(now);
            cpus.add(cpu);
        });

        machineEntity.setCpu(cpus.size());

        memoryDTOS.forEach(c -> {
            if (c.getCapacity() == 0) {
                return;
            }
            Memory memory = new Memory();
            memory.setMemModFrequency(c.getSpeed() + "");
            memory.setMemModType(c.getSpeed() > 1866 ? "DDR4" : "DDR3");
            memory.setMemModSize(c.getCapacity() + "");
            memory.setMemModNum(c.getMEMID() + "");
            memory.setSyncTime(now);
            memory.setSn(c.getSN());
            memories.add(memory);
        });
        machineEntity.setPmCpus(cpus);
        machineEntity.setPmMemories(memories);
    }

    @Override
    public Float getPowerMetric(String ip, String userName, String password) {
        return null;
    }

    @Override
    public PluginResult logout(String ip, String userName, String password) {
        if (login(ip, userName, password)) {
            try {
                Map headers = headerMap.get(ip);
                OkHttpUtils.getHttps(String.format(logoutUrl, ip), headers);
            } catch (Exception e) {
                return PluginResult.error("");
            }
            headerMap.remove(ip);
        }
        return PluginResult.success();
    }
}
