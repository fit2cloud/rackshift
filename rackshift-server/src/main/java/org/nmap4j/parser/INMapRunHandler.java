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

/**
 * This interface defines the functionality necessary to create the various
 * nmap XML objects based on the parsed data.  It's primary purpose is to
 * allow a discrete way to handle creating data objects from the XML data.
 * <p>
 * The methods defined here are specifically for loading the XML attributes
 * and not the child elements.  That is handled in the DefaultHandler
 * implementation.  In essence, this is a utility class for creating Objects 
 * from the XML attributes.
 * 
 * @author jsvede
 *
 */
public interface INMapRunHandler {
	
	public NMapRun createNMapRun( Attributes attributes ) ;
	
	public Host createHost( Attributes attributes ) ;
	
	public Distance createDistance( Attributes attributes ) ;

	public Address createAddress(  Attributes attributes ) ; 
	
	public Hostnames createHostnames(  Attributes attributes ) ;
	
	public Hostname createHostname( Attributes attributes ) ;
	
	public IpIdSequence createIpIdSequence(  Attributes attributes ) ;
	
	public Os createOs(  Attributes attributes ) ;
	
	public Ports createPorts(  Attributes attributes ) ;
	
	public Status createStatus(  Attributes attributes ) ;
	
	public TcpSequence createTcpSequence(  Attributes attributes ) ;
	
	public TcpTsSequence createTcpTsSequence(  Attributes attributes ) ;
	
	public Times createTimes(  Attributes attributes ) ;
	
	public Uptime createUptime(  Attributes attributes ) ;

	public OsClass createOsClass(  Attributes attributes ) ;
	
	public OsMatch createOsMatch(  Attributes attributes ) ;
	
	public PortUsed createPortUsed(  Attributes attributes ) ;
	
	public ExtraPorts createExtraPorts(  Attributes attributes ) ;
	
	public Port createPort(  Attributes attributes ) ;
	
	public Debugging createDebugging(  Attributes attributes ) ;
	
	public RunStats createRunStats(  Attributes attributes ) ;
	
	public ScanInfo createScanInfo(  Attributes attributes ) ;
	
	public Verbose createVerbose(  Attributes attributes ) ;
	
	public ExtraReasons createExtraReasons(  Attributes attributes ) ;
	
	public Service createService(  Attributes attributes ) ;
	
	public State createState(  Attributes attributes ) ;
	
	public Finished createFinished(  Attributes attributes ) ;
	
	public Hosts createHosts(  Attributes attributes ) ;
	
	public Cpe createCpe( Attributes attributes ) ;
	
	public Trace createTrace( Attributes attributes ) ;

	public Hop createHop( Attributes attributes ) ;

}
