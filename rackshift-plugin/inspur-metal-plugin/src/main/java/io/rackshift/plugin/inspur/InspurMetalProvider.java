package io.rackshift.plugin.inspur;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.rackshift.metal.sdk.AbstractMetalProvider;
import io.rackshift.metal.sdk.MetalPlugin;
import io.rackshift.metal.sdk.MetalPluginException;
import io.rackshift.metal.sdk.constants.BareMetalConstants;
import io.rackshift.metal.sdk.constants.F2CResourceTypeConstants;
import io.rackshift.metal.sdk.constants.ProtocolEnum;
import io.rackshift.metal.sdk.model.MachineEntity;
import io.rackshift.metal.sdk.model.Metric;
import io.rackshift.metal.sdk.model.PluginResult;
import io.rackshift.metal.sdk.model.request.IPMIRequest;
import io.rackshift.metal.sdk.util.*;
import io.rackshift.plugin.inspur.model.InspurFruDTO;
import io.rackshift.plugin.inspur.utils.IMS5280M4RestSpider;
import io.rackshift.plugin.inspur.utils.IMS8480M4RestSpider;
import io.rackshift.plugin.inspur.utils.IMSRestApi;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static io.rackshift.metal.sdk.constants.RackHDConstants.workflowPostUrl;


@MetalPlugin
public class InspurMetalProvider extends AbstractMetalProvider {

    public InspurMetalProvider() {
        super.name = "fit2cloud-inspur-metal-plugin";
    }

    private static Map<String, IMSRestApi> spiderMap = new HashMap() {{
        put("NF5280M4", new IMS5280M4RestSpider());
        put("NF8480M4", new IMS8480M4RestSpider());
    }};

    private static final String overviewUrl = "http://%s/rpc/getfruinfo.asp";
    private static final String getCookieUrl = "http://%s/rpc/WEBSES/create.asp";

    private static Gson gson;

    static {
        try {
            gson = new Gson();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> getHeader(String ip) {
        return null;
    }

    @Override
    public PluginResult login(String ipmiRequestStr) throws MetalPluginException {
        IPMIRequest request = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
        String ip = request.getHost();
        String userName = request.getUserName();
        String password = request.getPwd();
        checkIPMIParameter(request);
        try {
            if (IMSRestApi.headerMap.get(ip) != null) {
                return PluginResult.success();
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
            IMSRestApi.headerMap.put(ip, headers);
            return PluginResult.success();
        } catch (Exception e) {
            IMSRestApi.headerMap.remove(ip);
            LogUtil.error(String.format("爬取浪潮ip：{%s}时，获取sessionKey失败！e:[%s]", ip, ExceptionUtils.getExceptionDetail(e)));
        }
        return PluginResult.error("");
    }

    @Override
    public PluginResult logout(String ipmiRequestStr) throws MetalPluginException {
        IPMIRequest request = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
        String ip = request.getHost();
        String userName = request.getUserName();
        String password = request.getPwd();
        if (login(ipmiRequestStr).isSuccess()) {
            try {
                Map headers = IMSRestApi.headerMap.get(ip);
                String result = getResponseJSONString(OkHttpUtils.getHttps(String.format(overviewUrl, ip), headers));
                LogUtil.error(String.format("准备登出浪潮ip：{%s}时，获取overviewUrl:{%s}", ip, result));
                InspurFruDTO inspurFruDTO = gson.fromJson(result, InspurFruDTO.class);
                InspurFruDTO.WEBVARSTRUCTNAMEHLGETALLFRUINFOBean fruBean = inspurFruDTO.getWEBVARSTRUCTNAMEHLGETALLFRUINFO().get(0);
                if (spiderMap.get(fruBean.getPIProductName()) == null) {
                    MetalPluginException.throwException(String.format("该机型:%s不在插件支持列表中！", fruBean.getPIProductName()));
                }
                return spiderMap.get(fruBean.getPIProductName()).logout(ip, userName, password);
            } catch (Exception e) {
                LogUtil.error(String.format("准备登出浪潮ip:%s时发生异常%s   ", ip, ExceptionUtils.getExceptionDetail(e)));
                IMSRestApi.headerMap.remove(ip);
                return null;
            }
        } else {
            MetalPluginException.throwException(String.format("登出浪潮%s失败！", ip));
        }
        return PluginResult.success();
    }

    @Override
    public MachineEntity getMachineEntity(String ipmiRequestStr) throws MetalPluginException {
        IPMIRequest request = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
        String ip = request.getHost();
        String userName = request.getUserName();
        String password = request.getPwd();
        checkIPMIParameter(request);

        if (login(ipmiRequestStr).isSuccess()) {
            try {
                Map headers = IMSRestApi.headerMap.get(ip);
                String result = getResponseJSONString(OkHttpUtils.getHttps(String.format(overviewUrl, ip), headers));
                LogUtil.error(String.format("爬取浪潮ip：{%s}时，获取overviewUrl:{%s}", ip, result));
                InspurFruDTO inspurFruDTO = gson.fromJson(result, InspurFruDTO.class);
                InspurFruDTO.WEBVARSTRUCTNAMEHLGETALLFRUINFOBean fruBean = inspurFruDTO.getWEBVARSTRUCTNAMEHLGETALLFRUINFO().get(0);
                if (spiderMap.get(fruBean.getPIProductName()) == null) {
                    MetalPluginException.throwException(String.format("该机型:%s不在插件支持列表中！", fruBean.getPIProductName()));
                }
                return spiderMap.get(fruBean.getPIProductName()).getMachineEntity(ip, userName, password);
            } catch (Exception e) {
                LogUtil.error(String.format("爬取浪潮ip:%s时发生异常%s   ", ip, ExceptionUtils.getExceptionDetail(e)));
                IMSRestApi.headerMap.remove(ip);
                return null;
            }
        } else {
            MetalPluginException.throwException(String.format("登录浪潮%s失败！", ip));
        }
        return null;
    }

    @Override
    public MachineEntity getMachineEntityThroughSNMP(String ipmiRequestStr) throws MetalPluginException {
        return null;
    }

    @Override
    public MachineEntity getMachineEntityThroughRedfish(String ipmiRequestStr) throws MetalPluginException {
        return null;
    }

    private static String getResponseJSONString(String a) {
        String temp[] = a.split("\n");
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : temp) {
            if (s.startsWith("//")) continue;
            stringBuffer.append(s);
        }
        return stringBuffer.toString().replace(";", "").split("=")[1];
    }

    @Override
    public JSONObject getRaidPayLoad(String raidConfigDTOStr) throws MetalPluginException {
        JSONObject raidPayload = JSONObject.parseObject(raidConfigDTOStr);

        JSONObject createRaid = raidPayload.getJSONObject("options").getJSONObject("create-raid");
        JSONArray raidList = new JSONArray();

        for (int i = 0; i < createRaid.getJSONArray("raidList").size(); i++) {
            JSONObject c = createRaid.getJSONArray("raidList").getJSONObject(i);
            JSONObject raidConfigObj = new JSONObject();
            raidConfigObj.put("type", getValidRaidType(c.getString("type")));
            raidConfigObj.put("drives", c.getJSONArray("drives").stream().map(s -> ((String) s).replace("-", " ")).collect(Collectors.joining(" ")));
            raidList.add(raidConfigObj);
        }
        createRaid.put("raidList", raidList);
        return raidPayload;
    }

    @Override
    public JSONObject getDeleteRaidPayload() {
        try {
            return JSONObject.parseObject(getPageTemplate(F2CResourceTypeConstants.RACKHD_RAID_DEL_PAYLOAD));
        } catch (MetalPluginException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    public String getRaidWorkFlow() {
        return workflowPostUrl + "Graph.Raid.Create.AdaptecRAID";
    }

    @Override
    public String getDeleteRaidWorkFlow() {
        return workflowPostUrl + "Graph.Raid.Delete.AdaptecRAID";
    }

    @Override
    public String getCatalogRaidWorkFlow() {
        return workflowPostUrl + "Graph.Raid.Catalog.AdaptecRAID";
    }

    @Override
    public String getValidRaidType(String raidType) throws MetalPluginException {
        return raidType.replace("raid", "");
    }

    @Override
    public Metric getMetric(String ipmiRequestStr) throws MetalPluginException {
        try {
            Metric metric = new Metric();
            IPMIRequest request = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
            checkIPMIParameter(request);
            String ipmiSdr = IPMIUtils.exeCommand(request, "sdr");

            // 日志信息
            String selStr = IPMIUtils.exeCommand(request, "sel");
            if (StringUtils.isNotBlank(selStr)) {
                for (String s : selStr.split("\n")) {
                    if (StringUtils.isNotBlank(s) && s.contains("Percent Used")) {
                        metric.setSelPercentUsed(Long.valueOf(s.replace("%", "")
                                .replace("\"", "")
                                .replace(" ", "")
                                .split(":")[1]));
                    }
                }
            }

            //cpu温度
            String cpuTempStr = IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, "CPU\\d_T"), "\\|", 1, "degreesC");
            for (String s : cpuTempStr.split("\n")) {
                if ("noreading".equalsIgnoreCase(s)) {
                    continue;
                }
                metric.getCpuTemp().add(Double.valueOf(s).intValue());
            }

            //主板温度
            String mainBoardTempStr = Optional.ofNullable(IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, "INLET\\d_\\d"), "\\|", 1, "degreesC")).orElse(IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, "Inlet_Temp"), "\\|", 1, "degreesC"));
            for (String s : mainBoardTempStr.split("\n")) {
                if (!"disabled".equalsIgnoreCase(s))
                    metric.setMainBoardTemp(Double.valueOf(s).intValue());
            }

            //内存状态
            setMetricList(ipmiSdr, "MRB1_PVDDQ_CH\\d_\\d", "MEM_CH\\w\\d_Status", metric.getMemoryStatus(), "");

            //电源状态
            setMetricList(ipmiSdr, "PMBPower_\\d", "PSU\\d_Supply", metric.getPowerStatus(), "");

            //电源瓦特Total_Power
            String powerWattStr = Optional.ofNullable(IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, "PMBPower_\\d"), "\\|", 1, "Watts")).orElse(IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, "Total_Power"), "\\|", 1, "Watts"));
            for (String s : powerWattStr.split("\n")) {
                metric.getPowerWatt().add(Double.valueOf(s).intValue());
            }

            //风扇状态
            setMetricList(ipmiSdr, "FAN_\\d", metric.getFanStatus(), "");

            //磁盘状态
            setMetricList(ipmiSdr, "HDD\\d+_Status", metric.getDisktatus(), "");
            return metric;
        } catch (Exception e) {
            MetalPluginException.throwException("查询监控数据出现异常！" + ExceptionUtils.getExceptionDetail(e));
        }
        return null;
    }

    @Override
    public List<ProtocolEnum> getSupportedProtocol() {
        return new ArrayList<ProtocolEnum>() {{
            add(ProtocolEnum.HTTP);
        }};
    }

    private void setMetricList(String ipmiSdr, String regex1, String regex2, List<Integer> disktatus, String replaceVal) {
        String diskStatusStr = Optional.ofNullable(IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, regex1), "\\|", 2, replaceVal)).orElse(IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, regex2), "\\|", 2, replaceVal));
        if (StringUtils.isBlank(diskStatusStr)) return;
        for (String s : diskStatusStr.split("\n")) {
            disktatus.add("ok".equalsIgnoreCase(s) ? BareMetalConstants.HEALTHY : "ns".equalsIgnoreCase(s) || "nc".equalsIgnoreCase(s) ? BareMetalConstants.NOTDETECTED : BareMetalConstants.ERROR);
        }
    }

    private void setMetricList(String ipmiSdr, String regex1, List<Integer> disktatus, String replaceVal) {
        String diskStatusStr = IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, regex1), "\\|", 2, replaceVal);
        if (StringUtils.isBlank(diskStatusStr)) return;
        for (String s : diskStatusStr.split("\n")) {
            disktatus.add("ok".equalsIgnoreCase(s) ? BareMetalConstants.HEALTHY : "ns".equalsIgnoreCase(s) || "nc".equalsIgnoreCase(s) ? BareMetalConstants.NOTDETECTED : BareMetalConstants.ERROR);
        }
    }

}
