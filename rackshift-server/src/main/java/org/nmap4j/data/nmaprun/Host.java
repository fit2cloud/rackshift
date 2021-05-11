/*
 * Copyright (c) 2010, nmap4j.org
 *
 * All rights reserved.
 *
 * This license covers only the Nmap4j library.  To use this library with
 * Nmap, you must also comply with Nmap's license.  Including Nmap within
 * commercial applications or appliances generally requires the purchase
 * of a commercial Nmap license (see http://nmap.org/book/man-legal.html).
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright 
 *      notice, this list of conditions and the following disclaimer in the 
 *      documentation and/or other materials provided with the distribution.
 *    * Neither the name of the nmap4j.org nor the names of its contributors 
 *      may be used to endorse or promote products derived from this software 
 *      without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.nmap4j.data.nmaprun;

import java.util.ArrayList;

import org.nmap4j.data.host.Address;
import org.nmap4j.data.host.Distance;
import org.nmap4j.data.host.Hostnames;
import org.nmap4j.data.host.IpIdSequence;
import org.nmap4j.data.host.Os;
import org.nmap4j.data.host.Ports;
import org.nmap4j.data.host.Status;
import org.nmap4j.data.host.TcpSequence;
import org.nmap4j.data.host.TcpTsSequence;
import org.nmap4j.data.host.Times;
import org.nmap4j.data.host.Uptime;
import org.nmap4j.data.host.trace.Trace;


public class Host {
	
	public final static String HOST_TAG = "host" ;
	
	public final static String STARTTIME_ATTR = "starttime" ;
	public final static String ENDTIME_ATTR = "endtime" ;
	
	private long startTime ; 
	private long endTime ;
	
	private Status status ;
	private ArrayList<Address> addresses ; 
	private Ports ports ;
	private Os os ;
	private Uptime uptime ;
	private Distance distance ;
	private TcpSequence tcpSequence ;
	private IpIdSequence ipIdSequence;
	private TcpTsSequence tcpTsSequence;
	private Times times ;
	private Hostnames hostnames ;

	private Trace trace;
	public Trace getTrace() {
		return trace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	public Host() {
		addresses = new ArrayList<Address>() ;
	}
	
	public Hostnames getHostnames() {
		return hostnames;
	}
	public void setHostnames(Hostnames hostnames) {
		this.hostnames = hostnames;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	
	public ArrayList<Address> getAddresses() {
		return addresses;
	}
	public void setAddress(ArrayList<Address> address) {
		this.addresses = address;
	}
	public void addAddress( Address address ) {
		addresses.add( address ) ;
	}
	public Ports getPorts() {
		return ports;
	}
	public void setPorts(Ports ports) {
		this.ports = ports;
	}
	public Os getOs() {
		return os;
	}
	public void setOs(Os os) {
		this.os = os;
	}
	public Uptime getUptime() {
		return uptime;
	}
	public void setUptime(Uptime uptime) {
		this.uptime = uptime;
	}
	public Distance getDistance() {
		return distance;
	}
	public void setDistance(Distance distance) {
		this.distance = distance;
	}
	public TcpSequence getTcpSequence() {
		return tcpSequence;
	}
	public void setTcpSequence(TcpSequence tcpSequence) {
		this.tcpSequence = tcpSequence;
	}
	public IpIdSequence getIpIdSequence() {
		return ipIdSequence;
	}
	public void setIpIdSequence(IpIdSequence ipIdSequence) {
		this.ipIdSequence = ipIdSequence;
	}
	public TcpTsSequence getTcpTsSequence() {
		return tcpTsSequence;
	}
	public void setTcpTsSequence(TcpTsSequence tcpTsSequence) {
		this.tcpTsSequence = tcpTsSequence;
	}
	public Times getTimes() {
		return times;
	}
	public void setTimes(Times times) {
		this.times = times;
	}
	@Override
	public String toString() {
		return "Host [endTime=" + endTime + ", startTime=" + startTime + "]";
	}
	
	
	
	
}
