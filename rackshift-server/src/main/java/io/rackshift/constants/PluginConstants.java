package io.rackshift.constants;

import io.rackshift.mybatis.domain.SystemParameter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PluginConstants {
    public static Map<String, String> snmpCommunityKey = new LinkedHashMap<String, String>();
    public static Map<String, String> snmpPortKey = new LinkedHashMap<String, String>();
    public static SystemParameter defaultCommunity = new SystemParameter();
    public static SystemParameter defaultPort = new SystemParameter();

    public static final String Inspur = "Inspur";
    public static final String Dell_Inc = "Dell Inc.";
    public static final String DELL = "DELL";
    public static final String IBM = "IBM";
    public static final String HP = "HP";

    static {
        snmpCommunityKey.put("HP", "pm.snmp.hp.community");
        snmpCommunityKey.put("Dell Inc.", "pm.snmp.dell.community");
        snmpCommunityKey.put("DELL", "pm.snmp.dell.community");
        snmpCommunityKey.put("Inspur", "pm.snmp.inspur.community");
        snmpCommunityKey.put("Huawei", "pm.snmp.hauwei.community");
        snmpCommunityKey.put("IBM", "pm.snmp.ibm.community");

        snmpPortKey.put("HP", "pm.snmp.hp.port");
        snmpPortKey.put("Dell Inc.", "pm.snmp.dell.port");
        snmpPortKey.put("DELL", "pm.snmp.dell.port");
        snmpPortKey.put("Inspur", "pm.snmp.inspur.port");
        snmpPortKey.put("Huawei", "pm.snmp.hauwei.port");
        snmpPortKey.put("IBM", "pm.snmp.ibm.port");

        defaultCommunity.setParamValue("public");
        defaultPort.setParamValue("161");
    }

    public enum PluginType {
        HP_METAL_PLUGIN("fit2cloud-hp-metal-plugin",
                new ArrayList<String>() {{
                    add("HP");
                }},
                new LinkedHashMap<String, String>() {{
                    put("cmpuser", "Szse@ipmi307");
                    put("administrator", "Bo1701@sse");
                }}
        ),

        DELL_METAL_PLUGIN("fit2cloud-dell-metal-plugin",
                new ArrayList<String>() {{
                    add("DELL");
                    add("Dell Inc.");
                }},
                new LinkedHashMap<String, String>() {{
                    put("cmpuser", "Szse@ipmi307");
                    put("root", "Bo1701@sse");
                    put("root", "calvin");
                }}),

        INSPUR_METAL_PLUGIN("fit2cloud-inspur-metal-plugin",
                new ArrayList<String>() {{
                    add("Inspur");
                }},
                new LinkedHashMap<String, String>() {{
                    put("cmpuser", "Szse@ipmi307");
                    put("admin", "Bo1701@sse");
                    put("admin", "admin");
                }}),

        HUAWEI_METAL_PLUGIN("fit2cloud-huawei-metal-plugin",
                new ArrayList<String>() {
                    {
                        add("Huawei");
                    }
                },
                new LinkedHashMap<String, String>() {{
                    put("cmpuser", "Szse@ipmi307");
                    put("Administrator", "Bo1701@sse");
                    put("Administrator", "admin");
                }}
        ),

        IBM_METAL_PLUGIN("fit2cloud-ibm-metal-plugin",
                new ArrayList<String>() {
                    {
                        add("IBM");
                    }
                },
                new LinkedHashMap<String, String>() {{
                    put("cmpuser", "Szse@ipmi307");
                    put("USERID", "Bo1701@sse");
                }});

        private String pluginName;
        private List<String> supportedBrands;
        //该品牌默认的一些通用账号密码
        private Map<String, String> defaultIpmiAccount;

        public Map<String, String> getDefaultIpmiAccount() {
            return defaultIpmiAccount;
        }

        PluginType(String pluginName, List<String> supportedBrands, Map<String, String> defaultIpmiAccount) {
            this.pluginName = pluginName;
            this.supportedBrands = supportedBrands;
            this.defaultIpmiAccount = defaultIpmiAccount;
        }

        public static String getPluginByBrand(String brand) {
            for (PluginType value : values()) {
                if (value.supportedBrands.contains(brand)) {
                    return value.pluginName;
                }
            }
            throw new RuntimeException("不支持的机型！" + brand);
        }

        public static PluginType getPluginTypeByBrand(String brand) {
            for (PluginType value : values()) {
                if (value.supportedBrands.contains(brand)) {
                    return value;
                }
            }
            throw new RuntimeException("不支持的机型！" + brand);
        }
    }
}
