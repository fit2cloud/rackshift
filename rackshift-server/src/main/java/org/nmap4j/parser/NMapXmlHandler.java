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
 *
 */
package org.nmap4j.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.nmap4j.data.NMapRun;
import org.nmap4j.data.host.Address;
import org.nmap4j.data.host.Cpe;
import org.nmap4j.data.host.Distance;
import org.nmap4j.data.host.Hostnames;
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
import org.nmap4j.data.host.ports.Port;
import org.nmap4j.data.host.trace.Hop;
import org.nmap4j.data.host.trace.Trace;
import org.nmap4j.data.nmaprun.Debugging;
import org.nmap4j.data.nmaprun.Host;
import org.nmap4j.data.nmaprun.RunStats;
import org.nmap4j.data.nmaprun.ScanInfo;
import org.nmap4j.data.nmaprun.Verbose;
import org.nmap4j.data.nmaprun.host.ports.port.Service;
import org.nmap4j.data.nmaprun.host.ports.port.State;
import org.nmap4j.data.nmaprun.hostnames.Hostname;
import org.nmap4j.data.nmaprun.runstats.Finished;
import org.nmap4j.data.nmaprun.runstats.Hosts;
import org.nmap4j.parser.events.NMap4JParserEventListener;
import org.nmap4j.parser.events.ParserEvent;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is the implementation of the DefaultHandler and receives 
 * notifications from the SAX parser when nodes are parsed.
 * <p>
 * From the NMap4J API standpoint, there should be little reason for you
 * to hook directly into this class (though you can if you want to).
 * @author jsvede
 *
 */
public class NMapXmlHandler extends DefaultHandler {
	
	private static List<NMap4JParserEventListener> listeners ;
	
	private INMapRunHandler runHandler ;
	
	private long parseStartTime  = 0 ;
	
	private long parseEndTime = 0 ;
	
	// private member variables for creating the hierarchy
	private NMapRun nmapRun ;
	private ScanInfo scanInfo ;
	private Debugging debugging ;
	private Verbose verbose ;
	private Host host ;
	private Status status ;
	private Address address ;
	private Hostnames hostnames ;
	private Hostname hostname ;
	private Ports ports ;
	private Port port ;
	private State state ;
	private Service service ;
	private Os os ;
	private PortUsed portUsed ;
	private OsClass osClass ;
	private OsMatch osMatch ;
	private Distance distance ;
	private TcpSequence tcpSequence ;
    private TcpTsSequence tcpTsSequence ;	
    private Times times ;
    private Uptime uptime ;
    private RunStats runStats ;
    private Finished finished ;
	private Hosts hosts ;
	private Cpe cpe ;
	private Trace trace;
	private Hop hop;
	
	private boolean isCpeData = false ;
	
	private String previousQName ;

	public NMapXmlHandler( INMapRunHandler handler ) {
		listeners = new ArrayList<NMap4JParserEventListener>() ;
		runHandler = handler ;
	}
	
	private void fireEvent( Object payload ) {
		ParserEvent event = new ParserEvent( this, payload ) ;
		if( listeners != null && listeners.size() > 0 ) {
			Iterator<NMap4JParserEventListener> listenersIterator = listeners.iterator() ;
			while( listenersIterator.hasNext() ) {
				NMap4JParserEventListener listener = listenersIterator.next() ;
				if( listener != null ) {
					listener.parseEventNotification( event ) ;
				}
			}
		}
	}
	
	public static void addListener( NMap4JParserEventListener listener ) {
		if( listeners == null ) {
			 listeners = new ArrayList<NMap4JParserEventListener>() ;
		}
		listeners.add( listener ) ;
	}
	
	public static void removeListener( NMap4JParserEventListener listener ) {
		listeners.remove( listener ) ;
	}

	@Override
	public void startDocument() throws SAXException {
		parseStartTime = System.currentTimeMillis() ;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if( qName.equals( NMapRun.NMAPRUN_TAG ) ) {
			nmapRun = runHandler.createNMapRun( attributes ) ;
		}
		if( qName.equals( ScanInfo.SCANINFO_TAG ) ) {
			scanInfo = runHandler.createScanInfo(attributes) ;
			nmapRun.setScanInfo( scanInfo ) ;
		}
		if( qName.equals( Debugging.DEBUGGING_TAG ) ) {
			debugging = runHandler.createDebugging( attributes ) ;
			nmapRun.setDebugging( debugging ) ;
		}
		if( qName.equals( Verbose.VERBOSE_TAG ) ) {
			verbose = runHandler.createVerbose(attributes ) ;
			nmapRun.setVerbose(verbose ) ;
		}
		if( qName.equals( Host.HOST_TAG ) ) {
			host = runHandler.createHost( attributes ) ;
			nmapRun.addHost(host) ;
		}
		if( qName.equals( Status.STATUS_TAG ) ) {
			status = runHandler.createStatus( attributes ) ;
			host.setStatus( status ) ;
		}
		if( qName.equals( Address.ADDRESS_TAG ) ) {
			address = runHandler.createAddress(attributes ) ;
			host.addAddress( address ) ;
		}
		if( qName.equals( Hostnames.HOSTNAMES_TAG ) ) {
			hostnames = runHandler.createHostnames( attributes ) ;
			host.setHostnames( hostnames ) ;
		}
		if( qName.equals( Hostname.HOSTNAME_TAG ) ) {
			hostname = runHandler.createHostname( attributes ) ;
			hostnames.addHostname(hostname); ;
		}
		if( qName.equals( Ports.PORTS_TAG ) ) {
			ports = runHandler.createPorts(attributes ) ;
			host.setPorts(ports ) ;
		}
		if( qName.equals( Port.PORT_TAG ) ) {
			port = runHandler.createPort(attributes ) ;
			ports.addPort( port ) ;
		}
		if( qName.equals( State.STATE_TAG ) ) {
			state = runHandler.createState( attributes ) ;
			port.setState( state ) ;
		}
		if( qName.equals( Service.SERVICE_TAG ) ) {
			service = runHandler.createService(attributes ) ;
			port.setService( service ) ;
		}
		if( qName.equals( Os.OS_TAG ) ) {
			os = runHandler.createOs(attributes ) ;
			host.setOs( os ) ;
		}
		if( qName.equals( PortUsed.PORT_USED_TAG ) ) {
			portUsed = runHandler.createPortUsed( attributes ) ;
			os.addPortUsed( portUsed ) ;
		}
		if( qName.equals( OsClass.OSCLASS_TAG ) ) {
			osClass = runHandler.createOsClass( attributes ) ;
			os.addOsClass( osClass ) ;
		}
		if( qName.equals( OsMatch.OS_MATCH_TAG ) ) {
			osMatch = runHandler.createOsMatch( attributes ) ;
			os.addOsMatch( osMatch ) ;
		}
		if( qName.equals( Distance.DISTANCE_TAG ) ) {
			distance = runHandler.createDistance( attributes ) ;
			host.setDistance(distance ) ;
		}
		if( qName.equals( TcpSequence.TCP_SEQUENCE_TAG ) ) {
			tcpSequence = runHandler.createTcpSequence( attributes ) ;
			host.setTcpSequence( tcpSequence ) ;
		}
		if( qName.equals( TcpTsSequence.TCP_TS_SEQUENCE_TAG ) ) {
			tcpTsSequence = runHandler.createTcpTsSequence( attributes ) ;
			host.setTcpTsSequence( tcpTsSequence ) ;
		}
		if( qName.equals( Times.TIMES_TAG ) ) {
			times = runHandler.createTimes( attributes ) ;
			host.setTimes( times ) ;
		}
		if( qName.equals( Uptime.UPTIME_TAG  ) ) {
			uptime = runHandler.createUptime( attributes ) ;
			host.setUptime( uptime ) ;
		}
		if( qName.equals( RunStats.RUNSTATS_TAG ) ) {
			runStats = runHandler.createRunStats( attributes ) ;
			nmapRun.setRunStats( runStats ) ;
		}
		if( qName.equals( Finished.FINISHED_TAG ) ) {
			finished = runHandler.createFinished( attributes ) ;
			runStats.setFinished( finished ) ;
		}
		if( qName.equals( Hosts.HOSTS_TAG ) ) {
			hosts = runHandler.createHosts( attributes ) ;
			runStats.setHosts( hosts ) ;
		}
		if( qName.equals( Cpe.CPE_ATTR  ) ) {
			isCpeData = true ;
			cpe = runHandler.createCpe( attributes ) ;
			if( previousQName.equals( OsClass.OSCLASS_TAG ) )  {
				osClass.addCpe( cpe ) ;
			} else if( previousQName.equals( Service.SERVICE_TAG ) ) {
				
			}
		}
		if(qName.equals(Trace.TRACE_TAG)) {
			trace = runHandler.createTrace(attributes);
			host.setTrace(trace);
		}
		if(qName.equals(Hop.HOP_TAG)) {
			hop = runHandler.createHop(attributes);
			trace.addHop(hop);
		}
		// set the previousQName for comparison to later elements
		previousQName = qName ;
	}
	
	

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if( isCpeData ) {
			String cpeText = new String( ch, start, length ) ;
			cpe.setCpeData(cpeText) ;
			isCpeData = false ;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if( qName.equals( NMapRun.NMAPRUN_TAG ) ) {
			fireEvent( nmapRun ) ;
			nmapRun = null ;
		}
		if( qName.equals( ScanInfo.SCANINFO_TAG ) ) {
			fireEvent( scanInfo ) ;
			scanInfo = null ;
		}
		if( qName.equals( Debugging.DEBUGGING_TAG ) ) {
			fireEvent( debugging ) ;
			debugging = null ;
		}
		if( qName.equals( Verbose.VERBOSE_TAG ) ) {
			fireEvent( verbose ) ;
			verbose = null ;
		}
		if( qName.equals( Host.HOST_TAG ) ) {
			fireEvent( host ) ;
			host = null ;
		}
		if( qName.equals( Status.STATUS_TAG ) ) {
			fireEvent( status  ) ;
			status = null ;
		}
		if( qName.equals( Address.ADDRESS_TAG ) ) {
			fireEvent( address ) ;
			address = null ;
		}
		if( qName.equals( Hostname.HOSTNAME_TAG ) ) {
			fireEvent(hostname) ;
			hostname = null ;
		}
		if( qName.equals( Hostnames.HOSTNAMES_TAG ) ) {
			fireEvent(hostnames ) ;
			hostnames = null ;
		}
		if( qName.equals( Ports.PORTS_TAG ) ) {
			fireEvent( ports ) ;
			ports = null ;
		}
		if( qName.equals( Port.PORT_TAG ) ) {
			fireEvent( port ) ;
			port = null ;
		}
		if( qName.equals( State.STATE_TAG ) ) {
			fireEvent( state ) ;
			state = null ;
		}
		if( qName.equals( Service.SERVICE_TAG ) ) {
			fireEvent( service ) ;
			service = null ;
		}
		if( qName.equals( Os.OS_TAG ) ) {
			fireEvent( os ) ;
			os = null ;
		}
		if( qName.equals( PortUsed.PORT_USED_TAG ) ) {
			fireEvent( portUsed ) ;
			portUsed = null ;
		}
		if( qName.equals( OsClass.OSCLASS_TAG ) ) {
			fireEvent( osClass ) ;
			osClass = null ;
		}
		if( qName.equals( OsMatch.OS_MATCH_TAG ) ) {
			fireEvent( osMatch ) ;
			osMatch = null ;
		}
		if( qName.equals( Distance.DISTANCE_TAG ) ) {
			fireEvent( distance ) ;
			distance = null ;
		}
		if( qName.equals( TcpSequence.TCP_SEQUENCE_TAG ) ) {
			fireEvent( tcpSequence ) ;
			tcpSequence = null ;
		}
		if( qName.equals( TcpTsSequence.TCP_TS_SEQUENCE_TAG ) ) {
			fireEvent( tcpTsSequence ) ;
			tcpTsSequence = null ;
		}
		if( qName.equals( Times.TIMES_TAG ) ) {
			fireEvent( times ) ;
			times = null ;
		}
		if( qName.equals( Uptime.UPTIME_TAG  ) ) {
			fireEvent( uptime ) ;
			uptime = null ;
		}
		if( qName.equals( RunStats.RUNSTATS_TAG ) ) {
			fireEvent( runStats ) ;
			runStats = null ;
		}
		if( qName.equals( Finished.FINISHED_TAG ) ) {
			fireEvent( finished ) ;
			finished = null ;
		}
		if( qName.equals( Hosts.HOSTS_TAG ) ) {
			fireEvent( hosts ) ;
			hosts = null ;
		}
		if( qName.equals( Cpe.CPE_ATTR ) ) {
			fireEvent( cpe ) ;
			cpe  = null ;
		}
		if( qName.equals( Trace.TRACE_TAG) ) {
			fireEvent( trace ) ;
			trace  = null ;
		}
		if(qName.equals(Hop.HOP_TAG)) {
			fireEvent(hop);
			hop = null;
		}
	}

	@Override
	public void endDocument() throws SAXException {
		parseEndTime = System.currentTimeMillis() ;
	}
	
	public long getExecTime() {
		return parseEndTime - parseStartTime ;
	}
	
}
