package io.rackshift.job.model;

import io.rackshift.metal.sdk.model.request.IPMISnmpRequest;

public class ProtocolRequest extends IPMISnmpRequest {

    private String protocol;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
