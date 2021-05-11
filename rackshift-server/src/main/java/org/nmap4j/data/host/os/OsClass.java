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
package org.nmap4j.data.host.os;

import java.util.ArrayList;

import org.nmap4j.data.host.Cpe;

public class OsClass {
	
	public final static String OSCLASS_TAG = "osclass" ;
	
	public final static String TYPE_ATTR = "type" ;
	public final static String VENDOR_ATTR = "vendor" ;
	public final static String OSFAMILY_ATTR = "osfamily" ;
	public final static String OSGEN_ATTR = "osgen" ;
	public final static String ACCURACY_ATTR = "accuracy" ;
	
	private String type ;
	private String vendor ;
	private String osfamily ; 
	private String osgen ; 
	private String accuracy ;
	private ArrayList<Cpe> cpe ;
	
	public OsClass() {
		cpe = new ArrayList<Cpe>() ;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getOsfamily() {
		return osfamily;
	}
	public void setOsfamily(String osfamily) {
		this.osfamily = osfamily;
	}
	public String getOsgen() {
		return osgen;
	}
	public void setOsgen(String osgen) {
		this.osgen = osgen;
	}
	public String getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	public ArrayList<Cpe> getCpe() {
		return cpe;
	}
	public void setCpe(ArrayList<Cpe> cpe) {
		this.cpe = cpe;
	}
	public void addCpe( Cpe cpe ) {
		this.cpe.add( cpe ) ;
	}

	@Override
	public String toString() {
		return "OsClass [accuracy=" + accuracy + ", osfamily=" + osfamily
				+ ", osgen=" + osgen + ", type=" + type + ", vendor=" + vendor
				+ "]";
	}

}
