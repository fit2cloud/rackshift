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
package org.nmap4j.data.host;

import java.util.ArrayList;

import org.nmap4j.data.host.os.OsClass;
import org.nmap4j.data.host.os.OsMatch;
import org.nmap4j.data.host.os.PortUsed;


public class Os {
	
	public final static String OS_TAG = "os" ;

	private ArrayList<PortUsed> portUseds ;
	private ArrayList<OsMatch> osMatches ;
	private ArrayList<OsClass> osClasses ;
	
	public Os() {
		portUseds = new ArrayList<PortUsed>() ;
		osMatches = new ArrayList<OsMatch>() ;
		osClasses = new ArrayList<OsClass>() ;
	}
	
	public ArrayList<PortUsed> getPortUseds() {
		return portUseds;
	}
	public void setPortUseds(ArrayList<PortUsed> portUseds) {
		this.portUseds = portUseds;
	}
	public ArrayList<OsMatch> getOsMatches() {
		return osMatches;
	}
	public void setOsMatches(ArrayList<OsMatch> osMatches) {
		this.osMatches = osMatches;
	}
	public boolean addOsMatch(OsMatch o) {
		return osMatches.add(o);
	}
	public boolean removeOsMatch(OsMatch o) {
		return osMatches.remove(o);
	}
	public boolean addPortUsed(PortUsed o) {
		return portUseds.add(o);
	}
	public boolean removePortUsed(PortUsed o) {
		return portUseds.remove(o);
	}
	public ArrayList<OsClass> getOsClasses() {
		return osClasses;
	}
	public void setOsClasses(ArrayList<OsClass> osClasses) {
		this.osClasses = osClasses;
	}
	public boolean addOsClass(OsClass o) {
		return osClasses.add(o);
	}
	public boolean removeOsClass(OsClass o) {
		return osClasses.remove(o);
	}
	@Override
	public String toString() {
		return "Os []";
	}
}
