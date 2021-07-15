package io.rackshift.model;

import io.rackshift.mybatis.domain.OutBand;

public class KVMInfo {
    private String bareMetalId;
    private OutBand outBand;
    private String port;
    private String containerId;

    public KVMInfo(String bareMetalId, OutBand outBand, String port, String containerId) {
        this.bareMetalId = bareMetalId;
        this.outBand = outBand;
        this.containerId = containerId;
        this.port = port;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getBareMetalId() {
        return bareMetalId;
    }

    public void setBareMetalId(String bareMetalId) {
        this.bareMetalId = bareMetalId;
    }

    public OutBand getOutBand() {
        return outBand;
    }

    public void setOutBand(OutBand outBand) {
        this.outBand = outBand;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
