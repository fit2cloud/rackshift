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
package org.nmap4j.parser.util;

/**
 * Utility class that generates random Strings.
 * 
 * @author jsvede
 * 
 */
public class UidGenerator {

	public static String getBigId(String ident) {
		String rs = "" + Math.random();
		rs = rs.substring(rs.indexOf(".") + 1);
		int end = rs.indexOf("E");
		if (end == -1) {
			end = rs.length();
		}
		rs = rs.substring(0, end);
		long last2 = (long) System.currentTimeMillis();
		String s = ident + "-"
				+ java.math.BigInteger.valueOf(last2).toString(36)
				+ new java.math.BigInteger(rs).toString(36);

		return s.toUpperCase();
	}

	public static String getSmallId(String ident) {
		String rs = "" + Math.random();
		rs = rs.substring(rs.indexOf(".") + 1);
		int end = rs.indexOf("E");

		if (end == -1) {
			end = rs.length();
		}
		rs = rs.substring(0, end);
		long last2 = (long) System.currentTimeMillis();
		java.math.BigInteger bi1 = java.math.BigInteger.valueOf(last2);
		java.math.BigInteger bi2 = new java.math.BigInteger(rs);

		java.math.BigInteger bi3 = bi1.divide(java.math.BigInteger
				.valueOf(100000l));
		java.math.BigInteger bi4 = bi2.divide(java.math.BigInteger
				.valueOf(100000000l));

		bi3.abs();
		bi4.abs();

		String s = ident + bi3.toString(36) + bi4.toString(36);

		return s.toUpperCase();
	}
}
