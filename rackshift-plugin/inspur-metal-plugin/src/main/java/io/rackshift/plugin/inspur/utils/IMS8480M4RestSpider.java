package io.rackshift.plugin.inspur.utils;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.metal.sdk.model.*;
import io.rackshift.metal.sdk.util.ExceptionUtils;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.metal.sdk.util.LogUtil;
import io.rackshift.metal.sdk.util.OkHttpUtils;
import io.rackshift.plugin.inspur.model.InspurBMCDTO;
import io.rackshift.plugin.inspur.model.InspurFruDTO;
import io.rackshift.plugin.inspur.model.nf8480m4.NF8480M4CpuDTO;
import io.rackshift.plugin.inspur.model.nf8480m4.NF8480M4MemoryDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMS8480M4RestSpider extends AbstractIMSRestApi {
    private static final String overviewUrl = "http://%s/rpc/getfruinfo.asp";
    private static final String logoutUrl = "http://%s/rpc/WEBSES/logout.asp";
    private static final String getCookieUrl = "http://%s/rpc/WEBSES/create.asp";
    private static final String getBMCUrl = "http://%s/rpc/getalllancfg.asp";
    private static final String getHWUrl = "http://%s/rpc/getHWInfo.asp";

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
        List<NF8480M4CpuDTO.WEBVARSTRUCTNAMECPUINFOBean> cpuDTOS = new LinkedList<>();
        List<NF8480M4MemoryDTO.WEBVARSTRUCTNAMEGETMEMINFOBean> memoryDTOS = new LinkedList<>();
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

                Map<String, String> resultMap = getHWResponseJSONString(OkHttpUtils.getHttps(String.format(getHWUrl, ip), headers));
                LogUtil.error(String.format("爬取浪潮ip：{%s}时，getHWUrl:{%s}", ip, result));

                //cpu
                result = resultMap.get("WEBVAR_JSONVAR_CPUINFO");
                LogUtil.error(String.format("爬取浪潮ip：{%s}时，getCPUUrl:{%s}", ip, result));
                NF8480M4CpuDTO NF8480M4CpuDTO = gson.fromJson(wrapResult(result), NF8480M4CpuDTO.class);

                cpuDTOS = NF8480M4CpuDTO.getWEBVARSTRUCTNAMECPUINFO();

                if (cpuDTOS.size() > 0) {
                    if (StringUtils.isNotBlank(cpuDTOS.get(0).getCPUVersion())) {
                        LogUtil.error(String.format("爬取浪潮ip：{%s}时，CPU型号:{%s}", ip, cpuDTOS.get(0).getCPUVersion()));
                        machineEntity.setCpuFre((int) (Float.parseFloat(cpuDTOS.get(0).getCPUVersion().split("@")[1].replace("GHz", "")) * 1000) + "");
                        machineEntity.setCpuType(cpuDTOS.get(0).getCPUVersion());
                        machineEntity.setCore(1);
                        machineEntity.setThread(2);
                    }
                }
                //内存
                result = resultMap.get("WEBVAR_JSONVAR_GETMEMINFO");
                LogUtil.error(String.format("爬取浪潮ip：{%s}时，getMemoryUrl:{%s}", ip, result));
                NF8480M4MemoryDTO memoryDTO = gson.fromJson(result, NF8480M4MemoryDTO.class);
                memoryDTOS = memoryDTO.getWEBVARSTRUCTNAMEGETMEMINFO();

                //内存存两份一份存extendInfo作为直接展示 另一份存另一个表
                List<JSONObject> memoryDetails = new ArrayList<>();
                memoryDTOS.forEach(memory -> {
                    if (memory.getMemSize() == 0) {
                        return;
                    }
                    JSONObject memoryDetail = new JSONObject();
                    memoryDetail.put("memorySize", memory.getMemSize());
                    memoryDetail.put("memoryType", memory.getMemSpeed() > 1866 ? "DDR4" : "DDR3");
                    machineEntity.setMemoryType(memory.getMemSpeed() > 1866 ? "DDR4" : "DDR3");
                    memoryDetails.add(memoryDetail);
                });
                HashMap<String, String> extendInfo = new HashMap<>();
                extendInfo.put("memoryDetails", memoryDetails.toString());
                machineEntity.setExtendInfo(extendInfo);
                machineEntity.setMemory(memoryDTOS.stream().mapToInt(m -> m.getMemSize()).sum());


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
        return null;
    }

    private static String wrapResult(String result) {
        if (StringUtils.isBlank(result)) {
            return result;
        }
        StringBuffer s1 = new StringBuffer(result.replace("'", "\""));
        String s2 = new String(s1);
        Pattern p = Pattern.compile("WEBVAR_STRUCTNAME_\\w+|HAPI_STATUS");
        Matcher m = p.matcher(s1);
        while (m.find()) {
            s2 = s2.replace(m.group(), "\"" + m.group() + "\"");
        }
        return s2;
    }

    private static Map<String, String> getHWResponseJSONString(String a) {
        String temp[] = a.split("\n");
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : temp) {
            if (s.startsWith("//")) continue;
            stringBuffer.append(s);
        }
        Map value = new HashMap();
        String[] valuePair = stringBuffer.toString().replace("//Dynamic data end", "").replace("//Dynamic data start", "").split(";");
        for (String s : valuePair) {
            String[] keyValue = s.replace(" ", "").split("=");
            if (keyValue.length != 2) continue;
            value.put(keyValue[0], keyValue[1]);
        }
        return value;
    }

    private void setCPUandMemory(MachineEntity machineEntity, List<NF8480M4CpuDTO.WEBVARSTRUCTNAMECPUINFOBean> cpuDTOS, List<NF8480M4MemoryDTO.WEBVARSTRUCTNAMEGETMEMINFOBean> memoryDTOS) {
        List<Cpu> cpus = new LinkedList<>();
        List<Memory> memories = new LinkedList<>();
        long now = System.currentTimeMillis();
        cpuDTOS.forEach(c -> {
            if (StringUtils.isBlank(c.getCPUVersion())) return;
            Cpu cpu = new Cpu();
            cpu.setProcName(c.getCPUVersion());
//            cpu.setProcNumCores(c.getCoreNumber() + "");
//            cpu.setProcNumCoresEnabled(c.getCoreNumber() + "");
            cpu.setProcNumThreads(2 + "");
            cpu.setProcSocket(c.getCPUSocket() + "");
            cpu.setProcSpeed((int) (Float.parseFloat(cpuDTOS.get(0).getCPUVersion().split("@")[1].replace("GHz", "")) * 1000) + "");
            cpu.setSyncTime(now);
            cpus.add(cpu);
        });

        machineEntity.setCpu(cpus.size());

        memoryDTOS.forEach(c -> {
            if (c.getMemSize() == 0) {
                return;
            }
            Memory memory = new Memory();
            memory.setMemModFrequency(c.getMemSpeed() + "");
            memory.setMemModType(c.getMemType() == 26 ? "DDR4" : "Unknow");
            memory.setMemModSize(c.getMemSize() + "");
            memory.setMemModNum(c.getMemDimm() + "");
            memory.setSyncTime(now);
//            memory.setMemModPartNum(c.getMemDimm());
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
