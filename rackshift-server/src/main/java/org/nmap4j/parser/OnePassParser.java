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

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.nmap4j.data.NMapRun;
import org.nmap4j.parser.events.NMap4JParserEventListener;
import org.nmap4j.parser.events.ParserEvent;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



/**
 * The OnePassParser takes a document and parsers it all the way through and
 * returns a tree of Objects that represent the XML that was just parsed.
 * <p>
 * This type of parsing is best for smaller documents where the time to parse
 * it is small and the amount of data is less.  If you have a large XML
 * document, you may want to consider using the NotifyingParser which issues
 * an event listener model, allowing you to listen in for host objects as they
 * are parsed.
 * <p>
 * To use this class, pass in a either a File object or an InputStream and call
 * the parse() method.  You will receive a tree of objects that contain the 
 * contents of the XML.
 * 
 * @author jsvede
 *
 */
public class OnePassParser implements NMap4JParserEventListener {
	
	public static final int STRING_INPUT = 1 ;
	public static final int FILE_NAME_INPUT = 2 ;
	
	private  NMapRun nmapRun ;
	
	private INMapRunHandler nmrh ;
	private NMapXmlHandler nmxh ;
	
	
	public OnePassParser() {
		nmrh = new NMapRunHandlerImpl() ;
		nmxh = new NMapXmlHandler( nmrh ) ;	
	}
	
	public NMapRun parse( String input, int type  ) {
		
		NMapXmlHandler.addListener( this ) ;
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    try {
	    
	      //get a new instance of parser
	      SAXParser sp = spf.newSAXParser();
	      
	      if( type == STRING_INPUT ) {
			  StringReader strReader = new StringReader( input ) ;
			  InputSource source  = new InputSource (strReader ) ;

		      sp.parse( source, nmxh );
	    	  
	      } else if( type == FILE_NAME_INPUT ) {
	    	//parse the file and also register this class for call backs
		      sp.parse("file:" + input, nmxh );  
	      }
	      
	      
	      
	    }catch(SAXException se) {
	      se.printStackTrace();
	    }catch(ParserConfigurationException pce) {
	      pce.printStackTrace();
	    }catch (IOException ie) {
	      ie.printStackTrace();
	    }
	    
	    NMapXmlHandler.removeListener( this ) ;
	    
		return nmapRun ;
	}

	public void parseEventNotification(ParserEvent event) {
		//System.out.println( event.getPayload() ) ;
		if( event.getPayload() instanceof NMapRun ) {
			nmapRun = (NMapRun) event.getPayload() ;
		}
	}
	
	public void addListener(NMap4JParserEventListener aListener ) {
		NMapXmlHandler.addListener( aListener ) ; 
 	}
	
	public void removeListener( NMap4JParserEventListener aListener ) {
		NMapXmlHandler.removeListener( aListener ) ;
	}
	
}
