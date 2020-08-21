package io.rackshift.rackshiftproxy.model;

import java.time.LocalDateTime;

public class DHCPLease {
    private String ip;
    private String mac;
    private LocalDateTime startTime;

    public DHCPLease() {
    }

    public DHCPLease(String ip, String mac, LocalDateTime startTime) {
        this.ip = ip;
        this.mac = mac;
        this.startTime = startTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
