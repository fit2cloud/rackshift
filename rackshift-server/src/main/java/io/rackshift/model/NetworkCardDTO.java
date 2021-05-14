package io.rackshift.model;

import io.rackshift.mybatis.domain.NetworkCard;

public class NetworkCardDTO extends NetworkCard {
    private String switchName;
    private String switchPort;

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getSwitchPort() {
        return switchPort;
    }

    public void setSwitchPort(String switchPort) {
        this.switchPort = switchPort;
    }
}