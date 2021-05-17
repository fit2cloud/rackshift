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
package org.nmap4j.parser;

import org.nmap4j.data.NMapRun;
import org.nmap4j.data.host.Address;
import org.nmap4j.data.host.Cpe;
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
import org.nmap4j.data.host.os.OsClass;
import org.nmap4j.data.host.os.OsMatch;
import org.nmap4j.data.host.os.PortUsed;
import org.nmap4j.data.host.ports.ExtraPorts;
import org.nmap4j.data.host.ports.Port;
import org.nmap4j.data.host.trace.Hop;
import org.nmap4j.data.host.trace.Trace;
import org.nmap4j.data.nmaprun.Debugging;
import org.nmap4j.data.nmaprun.Host;
import org.nmap4j.data.nmaprun.RunStats;
import org.nmap4j.data.nmaprun.ScanInfo;
import org.nmap4j.data.nmaprun.Verbose;
import org.nmap4j.data.nmaprun.host.ports.extraports.ExtraReasons;
import org.nmap4j.data.nmaprun.host.ports.port.Service;
import org.nmap4j.data.nmaprun.host.ports.port.State;
import org.nmap4j.data.nmaprun.hostnames.Hostname;
import org.nmap4j.data.nmaprun.runstats.Finished;
import org.nmap4j.data.nmaprun.runstats.Hosts;
import org.xml.sax.Attributes;

public class NMapRunHandlerImpl implements INMapRunHandler {

	public Address createAddress(Attributes attributes) {
		Address address = new Address() ;
		address.setAddr( attributes.getValue( Address.ADDR_ATTR ) ) ;
		address.setAddrtype( attributes.getValue( Address.ADDRTYPE_ATTR ) ) ;
		address.setVendor( attributes.getValue( Address.VENDOR_ATTR ) ) ;
		return address ;
	}

	public Debugging createDebugging(Attributes attributes) {
		Debugging debugging = new Debugging() ;
		debugging.setLevel( Long.parseLong( attributes.getValue( Debugging.LEVEL_TAG ) ) ) ;
		return debugging ;
	}

	public Distance createDistance(Attributes attributes) {
		Distance distance = new Distance() ;
		distance.setValue( Long.parseLong( attributes.getValue( Distance.VALUE_ATTR ) ) ) ;
		return distance ;
	}

	public ExtraPorts createExtraPorts(Attributes attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	public ExtraReasons createExtraReasons(Attributes attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	public Host createHost(Attributes attributes) {
		Host host  = new Host() ;
		if( attributes.getValue( Host.STARTTIME_ATTR ) != null ) {
			host.setStartTime( Long.parseLong( attributes.getValue( Host.STARTTIME_ATTR ) ) ) ;	
		}
		if( attributes.getValue( Host.ENDTIME_ATTR ) != null ) {
			host.setEndTime( Long.parseLong( attributes.getValue( Host.ENDTIME_ATTR ) ) ) ;	
		}
		
		return host ;
	}

	public Hostnames createHostnames(Attributes attributes) {
		return new Hostnames() ;
	}

	public Hosts createHosts(Attributes attributes) {
		Hosts hosts = new Hosts() ;
		hosts.setDown( Long.parseLong( attributes.getValue( Hosts.DOWN_ATTR  ) ) ) ;
		hosts.setTotal( Long.parseLong( attributes.getValue( Hosts.TOTAL_ATTR ) ) ) ;
		hosts.setUp( Long.parseLong( attributes.getValue( Hosts.UP_ATTR ) ) ) ;
		
		return hosts ;
	}

	public IpIdSequence createIpIdSequence(Attributes attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	public NMapRun createNMapRun(Attributes attributes) {
		
		NMapRun run = new NMapRun() ;
		
		run.setArgs( attributes.getValue(NMapRun.ARGS_ATTR ) ) ;
		run.setScanner( attributes.getValue(NMapRun.SCANNER_ATTR ) ) ;
		run.setStart( attributes.getValue( NMapRun.START_ATTR ) ) ;
		run.setStartstr( attributes.getValue( NMapRun.STARTSTR_ATTR ) ) ;
		run.setVersion( attributes.getValue( NMapRun.VERSION_ATTR ) ) ;
		run.setXmloutputversion( attributes.getValue( NMapRun.XML_OUTPUT_VERSION_ATTR ) ) ;
		
		return run ;
	}

	public Os createOs(Attributes attributes) {
		return new Os() ;
	}

	public OsClass createOsClass(Attributes attributes) {
		OsClass osClass = new OsClass() ;
		osClass.setAccuracy( attributes.getValue( OsClass.ACCURACY_ATTR  ) ) ;
		osClass.setOsfamily( attributes.getValue( OsClass.OSFAMILY_ATTR ) ) ;
		osClass.setOsgen( attributes.getValue( OsClass.OSGEN_ATTR ) ) ;
		osClass.setType( attributes.getValue( OsClass.TYPE_ATTR ) ) ;
		osClass.setVendor( attributes.getValue( OsClass.VENDOR_ATTR ) ) ;
		
		return osClass ;
	}

	public OsMatch createOsMatch(Attributes attributes) {
		OsMatch osMatch = new OsMatch() ;
		osMatch.setAccuracy( attributes.getValue( OsMatch.ACCURACY_ATTR ) ) ;
		osMatch.setLine( attributes.getValue( OsMatch.LINE_ATTR ) ) ;
		osMatch.setName( attributes.getValue( OsMatch.NAME_ATTR ) ) ;
		return osMatch ;
	}

	public Port createPort(Attributes attributes) {
		Port port = new Port() ;
		port.setPortId( Long.parseLong( attributes.getValue( Port.PORTID_ATTR ) ) ) ;
		port.setProtocol( attributes.getValue( Port.PROTOCOL_ATTR ) ) ;
		return port ;
	}

	public PortUsed createPortUsed(Attributes attributes) {
		PortUsed portUsed = new PortUsed() ;
		portUsed.setPortid( attributes.getValue( PortUsed.PORTID_ATTR ) ) ;
		portUsed.setProto( attributes.getValue( PortUsed.PROTO_ATTR ) ) ;
		portUsed.setState( attributes.getValue( PortUsed.STATE_ATTR ) ) ;
		return portUsed ;
	}

	public Ports createPorts(Attributes attributes) {
		return new Ports() ;
	}

	public RunStats createRunStats(Attributes attributes) {
		return new RunStats() ;
	}

	public ScanInfo createScanInfo(Attributes attributes) {
		ScanInfo scan = new ScanInfo() ;
		
		scan.setNumservices( Long.parseLong( 
				         attributes.getValue( ScanInfo.NUM_SERVICES_ATTR ) ) ) ;
		scan.setProtocol( attributes.getValue( ScanInfo.PROTOCOL_ATTR ) ) ;
		scan.setServices( attributes.getValue( ScanInfo.SERVICES_ATTR ) ) ;
		scan.setType( attributes.getValue( ScanInfo.TYPE_ATTR ) ) ;
		
		return scan ;
	}

	public Service createService(Attributes attributes) {
		Service service = new Service() ;
		service.setConf( attributes.getValue( Service.CONF_ATTR ) ) ;
		service.setExtrainfo( attributes.getValue( Service.EXTRA_INFO_ATTR ) ) ;
		service.setMethod( attributes.getValue( Service.METHOD_ATTR ) ) ;
		service.setName( attributes.getValue( Service.NAME_ATTR ) ) ;
		service.setOsType( attributes.getValue( Service.OS_TYPE_ATTR ) ) ;
		service.setProduct( attributes.getValue( Service.PRODUCT_ATTR ) ) ;
		service.setVersion( attributes.getValue( Service.VERSION_ATTR ) ) ;
		return service ;
	}

	public State createState(Attributes attributes) {
		State state = new State() ;
		state.setState( attributes.getValue( State.STATE_ATTR ) ) ;
		state.setReason( attributes.getValue( State.REASON_ATTR ) ) ;
		state.setReason_ttl( Long.parseLong( attributes.getValue( State.REASON_TTL_ATTR ) ) ) ;

		return state ;
	}

	public Status createStatus(Attributes attributes) {
		Status status = new Status() ;
		status.setReason( attributes.getValue( Status.REASON_ATTR ) ) ;
		status.setState( attributes.getValue( Status.STATE_ATTR ) ) ;
		return status ;
	}

	public TcpSequence createTcpSequence(Attributes attributes) {
		TcpSequence tcpSequence = new TcpSequence() ;
		tcpSequence.setDifficulty( attributes.getValue( TcpSequence.DIFFICULTY_ATTR) ) ;
		tcpSequence.setIndex( Long.parseLong( attributes.getValue( TcpSequence.INDEX_ATTR ) ) ) ;
		tcpSequence.setValues( attributes.getValue( TcpSequence.VALUES_ATTR ) ) ;
		
		
		return tcpSequence ;
	}

	public TcpTsSequence createTcpTsSequence(Attributes attributes) {
		TcpTsSequence tcpTsSequence = new TcpTsSequence() ;
		tcpTsSequence.setaClass( attributes.getValue( TcpTsSequence.CLASS_ATTR ) ) ;
		tcpTsSequence.setValues( attributes.getValue( TcpTsSequence.VALUES_ATTR ) ) ;
		return tcpTsSequence ;
	}

	public Times createTimes(Attributes attributes) {
		Times times = new Times() ;
		times.setRttvar( Long.parseLong( attributes.getValue( Times.RTTVAR_ATTR ) ) ) ;
		times.setSrtt( Long.parseLong( attributes.getValue( Times.SRTT_ATTR ) ) ) ;
		times.setTo( Long.parseLong( attributes.getValue( Times.TO_ATTR ) ) ) ;
		
		return times ;
	}

	public Uptime createUptime(Attributes attributes) {
		Uptime uptime = new Uptime() ;
		uptime.setLastboot( attributes.getValue( Uptime.LASTBOOT_ATTR ) ) ;
		if( attributes.getValue( Uptime.UPTIME_TAG ) != null ) {
			uptime.setSeconds( Long.parseLong( attributes.getValue( Uptime.UPTIME_TAG ) ) ) ;	
		}
		
		return uptime ;
	}

	public Verbose createVerbose(Attributes attributes) {
		Verbose verbose = new Verbose() ;
		verbose.setLevel( Long.parseLong( attributes.getValue( Verbose.LEVEL_ATTR ) ) ) ;
		
		return verbose ;
	}

	public Finished createFinished(Attributes attributes) {
		Finished finished = new Finished() ;
		finished.setElapsed( attributes.getValue( Finished.ELAPSED_ATTR ) ) ;
		finished.setTime( attributes.getValue( Finished.TIME_ATTR ) ) ;
		finished.setTimestr( attributes.getValue( Finished.TIMESTR_ATTR ) ) ; 
		
		return finished ;
	}

	public Hostname createHostname(Attributes attributes) {
		Hostname hostname = new Hostname() ;
		hostname.setName( attributes.getValue( Hostname.NAME_ATTR ) ) ;
		hostname.setType( attributes.getValue( Hostname.TYPE_ATTR ) ) ;
		return hostname ;
	}

	@Override
	public Cpe createCpe(Attributes attributes) {
		Cpe cpe = new Cpe() ;
		
		
		return cpe ;
	}
	
	public Trace createTrace(Attributes attributes) {
		Trace trace = new Trace() ;
		trace.setPort( Long.parseLong(attributes.getValue(Trace.PORT_ATTR)));
		trace.setProtocol(attributes.getValue(Trace.PORTOCOL_ATTR));
		return trace ;
	}
	
	public Hop createHop(Attributes attributes) {
		Hop hop = new Hop() ;
		
		hop.setTtl(Integer.parseInt(attributes.getValue(Hop.TTL_ATTR)));
		hop.setIpaddr(attributes.getValue(Hop.IPADDR_ATTR));
		hop.setRtt(Float.parseFloat(attributes.getValue(Hop.RTT_ATTR)));;
		return hop ;
	}

}
