package io.rackshift.metal.sdk.model.request;

public class IPMISnmpRequest extends IPMIRequest {

    String community;
    int port;

    public IPMISnmpRequest() {
    }

    public IPMISnmpRequest(String host, String userName, String pwd) {
        super(host, userName, pwd);
    }

    public IPMISnmpRequest(String host, String userName, String pwd, String community, int port) {
        super(host, userName, pwd);
        this.community = community;
        this.port = port;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
