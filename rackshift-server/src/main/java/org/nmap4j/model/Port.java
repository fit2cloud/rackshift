package org.nmap4j.model;

/**
 * A class to flatten the port data structure that comes out of Nmap.
 * 
 * 
 * @author jon.svede
 * 
 * 
 *       <port protocol="tcp" portid="443">
        <state state="open" reason="syn-ack" reason_ttl="244"/>
        <service name="http" 
                 product="Microsoft IIS webserver" 
                 version="6.0" 
                 ostype="Windows" 
                 tunnel="ssl" 
                 method="probed" 
                 conf="10"/>
      </port> 
 *
 */
public class Port {

	private String protocol ;
	private int portNumber ;
	private String state ;
	private String reason ;
	private String reasonTtl ;
	private String serviceName ;
	private String product ;
	private String ostype ;
	private String tunnel ;
	private String method ;
	private String conf ;

	public Port( String prtcl, int portNum ) {
		protocol = prtcl ;
		portNumber = portNum ;
	}
	
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getReasonTtl() {
		return reasonTtl;
	}
	public void setReasonTtl(String reasonTtl) {
		this.reasonTtl = reasonTtl;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getConf() {
		return conf;
	}
	public void setConf(String conf) {
		this.conf = conf;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getOstype() {
		return ostype;
	}
	public void setOstype(String ostype) {
		this.ostype = ostype;
	}
	public String getTunnel() {
		return tunnel;
	}
	public void setTunnel(String tunnel) {
		this.tunnel = tunnel;
	}
}
