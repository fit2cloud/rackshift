package io.rackshift.constants;

import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.SystemParameter;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

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
}
