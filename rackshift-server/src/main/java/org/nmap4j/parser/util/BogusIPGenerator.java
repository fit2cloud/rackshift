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

import java.util.Random;


/**
 * This is a utility class that generates bogus IP addresses that are out of
 * range actually. The generated values have nothing to do with an input.
 * 
 * @author jsvede
 *
 */
public class BogusIPGenerator {
	
	private static Random random ;
	
	private static int base = 255 ;
	
	private static String DOT = "." ;
	
	private static Random getRandom() {
		if( random == null ) {
			random = new Random() ;
		}
		return random ;
	}
	
	public static int genIpNode() {
	
		int nextRandom = getRandom().nextInt( 744 ) ;
		
		return nextRandom + base ;
	}
	
	public static String getNodeAsString( int node ) {
		return Integer.toString( node ) ;
	}
	
	public static String generateBadIpAddress() {
		StringBuilder addrBuilder = new StringBuilder() ;
		for( int x=0; x<4; x++ ) {
			addrBuilder.append( getNodeAsString( genIpNode() ) ) ;
			if( x != 3 ) {
				addrBuilder.append( DOT ) ;
			}
			
		}
		return addrBuilder.toString();
	}

}
