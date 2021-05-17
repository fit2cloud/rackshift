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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is an implementation of the SAX parser's DefaultHandler which
 * basically receive's notifications of nodes from the SAX parser as nodes 
 * are encountered.
 * <p>
 * This implementation serves the specific purpose of obsfucating the IP address
 * and hostname values in the XML output of NMap.
 * 
 * @author jsvede
 *
 */
public class NMapXmlObsfucator extends DefaultHandler {

	private int indentIdx  = 0 ;
	
	/*
	 * TODO: replace the file with an agnostic reference. Also, this class's
	 * main method needs to be removed and an examples src tree needs to be 
	 * created.
	 * 
	 */
	private static String XML_FILE = "/Users/jsvede/work/loquatic/workspaces/nmap4j/org.nmap4j.parser/nmap-webapp-20090831-123159.xml" ;
	
	
	public static void main(String[] args) {
		NMapXmlObsfucator nmxo	 = new NMapXmlObsfucator() ;
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    try {
	    
	      //get a new instance of parser
	      SAXParser sp = spf.newSAXParser();
	      
	      if( args != null && args.length > 0 ) {
	    	 XML_FILE = args[0] ;
	      }
//	      String fileName = args[/0] ;
	      
	      //parse the file and also register this class for call backs
	      sp.parse("file:" + XML_FILE, nmxo );
	      
	    }catch(SAXException se) {
	      se.printStackTrace();
	    }catch(ParserConfigurationException pce) {
	      pce.printStackTrace();
	    }catch (IOException ie) {
	      ie.printStackTrace();
	    }
		
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		
		indentIdx-- ;
		
		StringBuffer indent = new StringBuffer() ;
		for( int x=0; x<indentIdx; x++  ) {
			indent.append( "\t" ) ; 
		}

		System.out.println( indent.toString() + "</" + qName + ">" ) ;
		
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		
//		System.out.println( "uri = " + uri ) ;
//		System.out.println( "localName = " + localName ) ;
		StringBuffer indent = new StringBuffer() ;
		for( int x=0; x<indentIdx; x++  ) {
			indent.append( "\t" ) ; 
		}
		System.out.print( indent.toString() + "<"  + qName + " " ) ;
		
		int lenAttr = attributes.getLength() ;
		
		for( int x=0; x<lenAttr; x++ ) {
			String name = attributes.getQName( x ) ;
			if( qName.equals( "hostname" ) ) {
				String fakeHostName = generateRandomHostName( null ) ;
				if( name.equals( "name" ) ) {
					System.out.print(  name + "=\"" + fakeHostName +"\" " ) ;
				}
			} else if( qName.equals( "address" ) ) {
				if( name.equals( "addr" ) )  {
					String addrType = attributes.getValue( "addrtype" ) ;
					if(  addrType != null && addrType.equals( "ipv4" ) ) {
						String bogusIpAddr = BogusIPGenerator.generateBadIpAddress() ;
						System.out.print( name + "=\"" + bogusIpAddr + "\" " ) ;
					} /*else if( addrType != null && addrType.equals( "ipv6" ) ) {
						// this is where we'd deal with ipv6
					
					}*/
				} else {
					System.out.print(  name + "=\"" + attributes.getValue( name ) +"\" " ) ;		
				}
			} else {
				System.out.print(  name + "=\"" + attributes.getValue( name ) +"\" " ) ;	
			}
			
		}
		System.out.print( ">\n" ) ;
		indentIdx++ ;
	}
	
	private String generateRandomHostName( String defaultDomainName ) {
		String myDefaultDomainName = "dummydomain.com" ;
		String uid = UidGenerator.getBigId( "host" ) ;
		
		StringBuffer nameBuffer = new StringBuffer() ;
		nameBuffer.append( uid ) ;
		nameBuffer.append( "." ) ;
		if( defaultDomainName != null ) {
			nameBuffer.append( defaultDomainName ) ;
		} else {
			nameBuffer.append( myDefaultDomainName ) ;
		}
		return nameBuffer.toString() ;
	}
	
}
