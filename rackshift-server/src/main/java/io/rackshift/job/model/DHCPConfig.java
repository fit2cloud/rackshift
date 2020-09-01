package io.rackshift.job.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class DHCPConfig {
    private String startIp;
    private String endIp;
    private String netmask;
    private String gateway;
    private boolean pxeEnabled;

    public DHCPConfig() {
    }

    public String getNetSegment() {
        String[] splitStartIip = startIp.split("\\.");
        String[] splitNetmask = netmask.split("\\.");
        for (int i = 0; i < 4; i++) {
            splitStartIip[i] = String.valueOf(Integer.valueOf(splitStartIip[i]) & Integer.valueOf(splitNetmask[i]));
        }
        return StringUtils.join(splitStartIip, ".");
    }

    public String getStartIp() {
        return startIp;
    }

    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    public String getEndIp() {
        return endIp;
    }

    public void setEndIp(String endIp) {
        this.endIp = endIp;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public boolean isPxeEnabled() {
        return pxeEnabled;
    }

    public void setPxeEnabled(boolean pxeEnabled) {
        this.pxeEnabled = pxeEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DHCPConfig config = (DHCPConfig) o;
        return pxeEnabled == config.pxeEnabled &&
                Objects.equals(startIp, config.startIp) &&
                Objects.equals(endIp, config.endIp) &&
                Objects.equals(netmask, config.netmask) &&
                Objects.equals(gateway, config.gateway);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startIp, endIp, netmask, gateway, pxeEnabled);
    }
}
