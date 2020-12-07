package io.rackshift.metal.sdk;

import com.google.gson.Gson;
import io.rackshift.metal.sdk.constants.BareMetalConstants;
import io.rackshift.metal.sdk.model.Metric;
import io.rackshift.metal.sdk.model.PluginResult;
import io.rackshift.metal.sdk.model.request.IPMIRequest;
import io.rackshift.metal.sdk.model.request.IPMIResetIpRequest;
import io.rackshift.metal.sdk.model.request.IPMIResetPwdRequest;
import io.rackshift.metal.sdk.model.request.IPMISnmpRequest;
import io.rackshift.metal.sdk.util.IPMIUtils;
import io.rackshift.metal.sdk.util.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public abstract class AbstractMetalProvider implements IMetalProvider {
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected String name;
    protected static Gson gson;
    //监控范围定义
    protected static String cpuTemperature = "cpuTemperature";
    protected static String memoryTemperature = "memoryTemperature";
    protected static String mainBoardTemperature = "mainBoardTemperature";
    protected static String powerSupplyTemperature = "powerSupplyTemperature";
    protected static String powerStatus = "powerStatus";
    protected static String fanStatus = "fanStatus";

    static {
        gson = new Gson();
    }

    @Override
    public PluginResult login(String ipmiRequestStr) throws MetalPluginException {
//        MetalPluginException.throwException(getName() + "暂时不支持login！");
        return PluginResult.success();
    }

    @Override
    public PluginResult logout(String ipmiRequestStr) throws MetalPluginException {
//        MetalPluginException.throwException(getName() + "暂时不支持logout！");
        return PluginResult.success();
    }

    @Override
    public String getName() {
        return this.name;
    }

    protected void checkIPMIParameter(IPMIRequest request) {
        if (request == null || StringUtils.isAnyBlank(request.getHost(), request.getUserName(), request.getPwd())) {
            MetalPluginException.throwException("参数错误！");
        }
    }

    protected void checkSnmpParameter(IPMISnmpRequest request) {
        if (request == null || StringUtils.isAnyBlank(request.getHost(), request.getCommunity()) || request.getPort() == 0) {
            MetalPluginException.throwException("参数错误！");
        }
    }

    protected void checkIPMISnmpParameter(IPMISnmpRequest request) {
        if (request == null || StringUtils.isAnyBlank(request.getHost(), request.getCommunity(), request.getUserName(), request.getPwd()) || request.getPort() == 0) {
            MetalPluginException.throwException("参数错误！");
        }
    }

    public String getPageTemplate() throws MetalPluginException {
        return getPageTemplate(null);
    }

    public String getPageTemplate(String resourceType) throws MetalPluginException {
        String pageFile = "raid_payload.json";
        if (StringUtils.isNoneBlank(resourceType)) {
            pageFile = resourceType;
        }
        return readConfigFile(pageFile);
    }

    public String getCredentialPageTemplate() throws MetalPluginException {
        return readConfigFile("credential.json");
    }

    @SuppressWarnings("resource")
    private String readConfigFile(String fileName) throws MetalPluginException {
        InputStream is = null;
        BufferedReader br = null;
        try {
            URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
            JarFile jarFile = new JarFile(url.getPath());
            is = jarFile.getInputStream(jarFile.getEntry(fileName));
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("读取json文件失败", e);
            throw new MetalPluginException("该插件尚不支持该操作!");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
                is = null;
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
                br = null;
            }
        }
    }

    @Override
    public PluginResult powerOn(String ipmiRequestStr) throws MetalPluginException {
        try {
            IPMIRequest account = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
            String commandResult = IPMIUtils.exeCommand(account, "power status");
            if (IpUtil.ping(account.getHost())) {
                if (commandResult.contains("Down") || commandResult.contains("Off") || commandResult.contains("off")) {
                    IPMIUtils.exeCommand(account, "power on");
                    return PluginResult.success();
                } else if (commandResult.contains("Chassis Power is on")) {
                    return PluginResult.success();
                } else {
                    return PluginResult.error("检查物理机:" + account.getHost() + "带外连通性失败！IPMI协议返回内容格式不正确！" + commandResult);
                }
            } else {
                return PluginResult.error("检查物理机:" + account.getHost() + "带外连通性失败！IP不通！");
            }
        } catch (Exception e) {
            throw new MetalPluginException(e);
        }
    }

    @Override
    public PluginResult powerOff(String ipmiRequestStr) throws MetalPluginException {
        try {
            IPMIRequest account = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
            String commandResult = IPMIUtils.exeCommand(account, "power status");
            if (IpUtil.ping(account.getHost())) {
                if (commandResult.contains("Down") || commandResult.contains("Off")) {
                    return PluginResult.success();
                } else if (commandResult.contains("Chassis Power is on")) {
                    commandResult = IPMIUtils.exeCommand(account, "power off");
                    return PluginResult.success();
                } else {
                    return PluginResult.error("检查物理机:" + account.getHost() + "带外连通性失败！IPMI协议返回内容格式不正确！" + commandResult);
                }
            } else {
                return PluginResult.error("检查物理机:" + account.getHost() + "带外连通性失败！IP不通！");
            }
        } catch (Exception e) {
            throw new MetalPluginException(e);
        }
    }

    @Override
    public PluginResult reset(String ipmiRequestStr) throws MetalPluginException {
        try {
            IPMIRequest account = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
            String commandResult = IPMIUtils.exeCommand(account, "power status");

            if (commandResult.contains(BareMetalConstants.PM_POWER_ON) || commandResult.contains("On")) {
                IPMIUtils.exeCommand(account, "power reset soft");
            } else {
                IPMIUtils.exeCommand(account, "power on");
            }
            return PluginResult.success();
        } catch (Exception e) {
            throw new MetalPluginException(e);
        }
    }

    @Override
    public PluginResult resetPXE(String ipmiRequestStr) throws MetalPluginException {
        try {
            IPMIRequest account = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
            String commandResult = IPMIUtils.exeCommand(account, "power status");
            IPMIUtils.exeCommand(account, "chassis bootdev pxe");
            if (commandResult.contains(BareMetalConstants.PM_POWER_ON) || commandResult.contains("On")) {
                IPMIUtils.exeCommand(account, "chassis bootdev pxe");
                IPMIUtils.exeCommand(account, "power reset");
                return PluginResult.success("");
            } else if (commandResult.contains(BareMetalConstants.PM_POWER_OFF) || commandResult.contains("Off")) {
                IPMIUtils.exeCommand(account, "power on");
                IPMIUtils.exeCommand(account, "chassis bootdev pxe");
                IPMIUtils.exeCommand(account, "power reset");
                return PluginResult.success();
            }
            return PluginResult.error("ipmi命令调用失败！可能是网络不通或者账号密码错误或带外异常！");
        } catch (Exception e) {
            throw new MetalPluginException(e);
        }
    }

    @Override
    public PluginResult status(String ipmiRequestStr) throws MetalPluginException {
        try {
            IPMIRequest account = gson.fromJson(ipmiRequestStr, IPMIRequest.class);
            String commandResult = IPMIUtils.exeCommand(account, "power status");
            if (IpUtil.ping(account.getHost())) {
                if (commandResult.contains("Down") || commandResult.contains("Off") || commandResult.contains("Chassis Power is on") || commandResult.contains("off")) {
                    return PluginResult.success();
                } else {
                    return PluginResult.error("检查物理机:" + account.getHost() + "带外连通性失败！IPMI协议返回内容格式不正确！" + commandResult);
                }
            } else {
                return PluginResult.error("检查物理机:" + account.getHost() + "带外连通性失败！IP不通！");
            }
        } catch (Exception e) {
            throw new MetalPluginException(e);
        }
    }

    @Override
    public PluginResult resetPwd(String ipmiResetPwdRequest) throws MetalPluginException {
        try {
            IPMIResetPwdRequest resetPwdRequest = gson.fromJson(ipmiResetPwdRequest, IPMIResetPwdRequest.class);
            String userIndex = IPMIUtils.exeCommandForUserIndex(resetPwdRequest.getBrand(), resetPwdRequest);
            int tryTimes = 0;
            do {
                if (userIndex.contains("Error")) {
                    userIndex = IPMIUtils.exeCommandForUserIndex(resetPwdRequest.getBrand(), resetPwdRequest);
                } else {
                    break;
                }
                tryTimes++;
            } while (tryTimes < 5);
            String commandResult = IPMIUtils.exeCommand(resetPwdRequest, String.format("user set password %s %s", userIndex, resetPwdRequest.getNewPwd()));
            if (commandResult.contains("successful") || StringUtils.isBlank(commandResult)) {
                return PluginResult.success();
            } else {
                return PluginResult.success("修改密码失败！合法的密码应该包含大小写字母数字特殊字符，总共不少于8个");
            }
        } catch (Exception e) {
            throw new MetalPluginException(e);
        }
    }

    @Override
    public PluginResult resetIp(String ipmiRequestStr) throws MetalPluginException {
        try {
            IPMIResetIpRequest account = gson.fromJson(ipmiRequestStr, IPMIResetIpRequest.class);
            if (StringUtils.isNotBlank(account.getNewIp())) {
                String result1 = null;
                if (!account.getBrand().equalsIgnoreCase("inspur")) {
                    try {
                        result1 = IPMIUtils.exeCommand(account, "lan set 1 ipsrc static");
                    } catch (Exception e) {
                        if (StringUtils.isBlank(result1) || result1.contains("failed")) {
                            String result2 = IPMIUtils.exeCommand(account, String.format("lan set 1 ipaddr %s", account.getNewIp()));
                            if (result2.contains("failed") || result2.contains("Setting")) {
                                return PluginResult.success("");
                            }
                        }
                    }
                } else {
                    try {
                        result1 = IPMIUtils.exeCommand(account, "lan set 2 ipsrc static");
                    } catch (Exception e) {
                        if (StringUtils.isBlank(result1) || result1.contains("failed")) {
                            String result2 = IPMIUtils.exeCommand(account, String.format("lan set 2 ipaddr %s", account.getNewIp()));
                            if (result2.contains("failed") || result2.contains("Setting")) {
                                IPMIUtils.exeCommand(account, "mc reset cold");
                                return PluginResult.success("");
                            }
                        }
                    }
                }
            }
            return PluginResult.error("ipmi修改IP失败！");
        } catch (Exception e) {
            throw new MetalPluginException(e);
        }
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
            String cpuTempStr = IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, "CPU\\d_TEMP"), "\\|", 1, "degreesC");
            for (String s : cpuTempStr.split("\n")) {
                metric.getCpuTemp().add(Integer.valueOf(s));
            }

            //主板温度
            String mainBoardTempStr = IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, "INLET\\d_\\d"), "\\|", 1, "degreesC");
            for (String s : mainBoardTempStr.split("\n")) {
                if (!"disabled".equalsIgnoreCase(s))
                    metric.setMainBoardTemp(Integer.valueOf(s));
            }

            //内存状态
            setMetricList(ipmiSdr, "MRB\\d_PVDDQ_CH\\d_\\d", metric.getMemoryStatus(), "");

            //电源状态
            setMetricList(ipmiSdr, "PMBPower_\\d", metric.getPowerStatus(), "");

            //电源瓦特
            String powerWattStr = IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, "PMBPower_\\d"), "\\|", 1, "Watts");
            for (String s : powerWattStr.split("\n")) {
                metric.getPowerWatt().add(Integer.valueOf(s));
            }

            //风扇状态
            setMetricList(ipmiSdr, "FAN_\\d", metric.getFanStatus(), "");

            //磁盘状态
            setMetricList(ipmiSdr, "HDD\\d_Status", metric.getDisktatus(), "");
            return metric;
        } catch (Exception e) {
            MetalPluginException.throwException("查询监控数据出现异常！" + e);
        }
        return null;
    }

    private void setMetricList(String ipmiSdr, String s2, List<Integer> disktatus, String replaceVal) {
        String diskStatusStr = IPMIUtils.extractSdrIndexValue(IPMIUtils.grep(ipmiSdr, s2), "\\|", 2, replaceVal);
        for (String s : diskStatusStr.split("\n")) {
            disktatus.add("ok".equalsIgnoreCase(s) ? BareMetalConstants.HEALTHY : BareMetalConstants.ERROR);
        }
    }

    public <T> T invokeCustomMethod(String methodName, Object... parameters) throws MetalPluginException {
        try {
            List<Class> paramsClass = new ArrayList<Class>();
            for (Object param : parameters) {
                paramsClass.add(param.getClass());
            }
            Method targetMethod = this.getClass().getDeclaredMethod(methodName, paramsClass.toArray(new Class[]{}));
            return (T) targetMethod.invoke(this, parameters);
        } catch (Exception e) {
            throw new MetalPluginException(e);
        }
    }
}
