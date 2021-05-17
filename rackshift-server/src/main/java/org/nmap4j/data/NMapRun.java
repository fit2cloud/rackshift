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
package org.nmap4j.data;

import java.util.ArrayList;

import org.nmap4j.data.nmaprun.Debugging;
import org.nmap4j.data.nmaprun.Host;
import org.nmap4j.data.nmaprun.RunStats;
import org.nmap4j.data.nmaprun.ScanInfo;
import org.nmap4j.data.nmaprun.Verbose;

/**
 * This class maps to the root element of NMap's XML output.
 * 
 * @author jsvede
 *
 */
public class NMapRun {
	
	public final static String NMAPRUN_TAG = "nmaprun" ;
	
	public final static String SCANNER_ATTR = "scanner" ;
	public final static String ARGS_ATTR = "args" ;
	public final static String START_ATTR = "start" ;
	public final static String STARTSTR_ATTR = "startstr" ;
	public final static String VERSION_ATTR = "version" ;
	public final static String XML_OUTPUT_VERSION_ATTR = "xmloutputversion" ;
	
	// attributes
	private String scanner ;
	private String args ;
	private String start ;
	private String startstr ;
	private String version ;
	private String xmloutputversion ;
	
	// child elements
	private ScanInfo scanInfo ;
	private Verbose verbose ;
	private Debugging debugging ;
	private RunStats runStats ;
	private ArrayList<Host> hosts ;
	
	public NMapRun() { 
		hosts = new ArrayList<Host>() ;
	}
	
	public ScanInfo getScanInfo() {
		return scanInfo;
	}
	public void setScanInfo(ScanInfo scanInfo) {
		this.scanInfo = scanInfo;
	}
	public Verbose getVerbose() {
		return verbose;
	}
	public void setVerbose(Verbose verbose) {
		this.verbose = verbose;
	}
	public Debugging getDebugging() {
		return debugging;
	}
	public void setDebugging(Debugging debugging) {
		this.debugging = debugging;
	}
	public RunStats getRunStats() {
		return runStats;
	}
	public void setRunStats(RunStats runStats) {
		this.runStats = runStats;
	}
	public ArrayList<Host> getHosts() {
		return hosts;
	}
	public void setHosts(ArrayList<Host> hosts) {
		this.hosts = hosts;
	}
	public void removeHost( Host host ) {
		hosts.remove( host ) ;
	}
	public void addHost( Host host ) {
		if( !hosts.contains( host ) ) {
			hosts.add( host ) ;
		}
	}
	public String getScanner() {
		return scanner;
	}
	public void setScanner(String scanner) {
		this.scanner = scanner;
	}
	public String getArgs() {
		return args;
	}
	public void setArgs(String args) {
		this.args = args;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getStartstr() {
		return startstr;
	}
	public void setStartstr(String startstr) {
		this.startstr = startstr;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getXmloutputversion() {
		return xmloutputversion;
	}
	public void setXmloutputversion(String xmloutputversion) {
		this.xmloutputversion = xmloutputversion;
	}

	@Override
	public String toString() {
		return "NMapRun [args=" + args + ", scanner=" + scanner + ", start="
				+ start + ", startstr=" + startstr + ", version=" + version
				+ ", xmloutputversion=" + xmloutputversion + 
				"\n\t hosts = " + hosts.size() +
				
				"]";
	}
	
	
	
	
}
