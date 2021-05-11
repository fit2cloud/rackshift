package org.nmap4j.data.host.trace;

import org.nmap4j.data.nmaprun.Host;

/**
 * This class has hop record on tracrout result
 * 
 * @author omark
 *
 */
public class Hop {
	public final static String HOP_TAG = "hop" ;
	public final static String TTL_ATTR = "ttl" ;
	public final static String IPADDR_ATTR= "ipaddr" ;
	public final static String RTT_ATTR = "rtt" ;
	
	
	private int ttl;
	private String ipaddr;
	private float rtt;
	private Host host;
	
	public int getTtl() {
		return ttl;
	}
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	public String getIpaddr() {
		return ipaddr;
	}
	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	public float getRtt() {
		return rtt;
	}
	public void setRtt(float rtt) {
		this.rtt = rtt;
	}
	
	
	public Host getHost() {
		return host;
	}
	public void setHost(Host host) {
		this.host = host;
	}
	@Override
	public String toString() {
		return "Hop [ttl=" + ttl + ", ipaddr=" + ipaddr + ", rtt=" + rtt + "]";
	}

}
