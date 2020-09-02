package io.rackshift.rackshiftproxy.model;

import io.rackshift.metal.sdk.util.IpUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        DHCPConfig n = (DHCPConfig) o;
        List<String> ipRanges = IpUtil.getIpRange(n.getStartIp(), n.getEndIp(), n.getStartIp(), n.getNetmask());
        List<String> ipRanges2 = IpUtil.getIpRange(this.getStartIp(), this.getEndIp(), this.getStartIp(), this.getNetmask());
        int total = ipRanges.size() + ipRanges2.size();
        Set ipSet = new HashSet<String>();
        ipSet.addAll(ipRanges);
        ipSet.addAll(ipRanges2);
        if (total != ipSet.size()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(this.getNetSegment().split("\\.")[0]);
    }
}
