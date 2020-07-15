package io.rackshift.metal.sdk.constants;

public enum ProtocolEnum {
    SNMP("snmp"),
    HTTP("http"),
    REDFISH("redfish");
    private String name;

    ProtocolEnum(String name) {
        this.name = name;
    }
}
