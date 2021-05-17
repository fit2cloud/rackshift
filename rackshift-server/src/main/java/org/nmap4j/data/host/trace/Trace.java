package org.nmap4j.data.host.trace;

import java.util.ArrayList;

/**
 * This class catches tracerout result if was enabled on scan command
 * 
 * @author omark
 *
 */
public class Trace {
	public final static String TRACE_TAG = "trace" ;
	public final static String PORT_ATTR = "port" ;
	public final static String PORTOCOL_ATTR = "proto" ;
	
	private long port;
	private String protocol;
	private ArrayList<Hop> hops = new ArrayList<>();

	public long getPort() {
		return port;
	}

	public void setPort(long port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public ArrayList<Hop> getHops() {
		return hops;
	}

	public void setHops(ArrayList<Hop> hops) {
		this.hops = hops;
	}
	
	public void addHop( Hop p ) {
		if( !hops.contains( p ) ) {
			hops.add( p ) ;
		}
	}

	public void remove( Hop p ) {
		if( hops.contains( p ) ) {
			hops.remove( p ) ;
		}
	}


	@Override
	public String toString() {
		return "Trace [port=" + port + ", protocol=" + protocol + ", hops="
				+ hops + "]";
	}

}
