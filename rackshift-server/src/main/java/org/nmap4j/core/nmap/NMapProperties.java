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
package org.nmap4j.core.nmap;

import java.io.File ;

import org.nmap4j.core.flags.Flag;

/**
 * This class is used to manage the path to nmap.  This class attempts to look
 * for the environmental variable NMAP_HOME.  If that is not set it is expected 
 * that the user set this location via this class.
 * <p>
 * Essentially the path needs to be to the location where the "bin" dir that 
 * contains the nmap binary is.  Usually, when dealing with most nmap installs,
 * this will also be location where the "share" dir is located.  It's ideal
 * to identify both of these of dirs as this allows you to specify the right
 * share dir for a binary.  Otherwise there is a risk that nmap will pick up
 * a default in the system and this may introduce inconsistencies.
 * <p>
 * If you are planning to use the system property NMAP_HOME to set your 
 * NMap path, use the no arg constructor.  Otherwise use the constructor with
 * the String. If you want flexibility,  set the path, use the no arg 
 * constructor and then use the setPath(String) method.
 * 
 * @author jsvede
 *
 */
public class NMapProperties {
  
  private String pathToNMap ;
  
  private final String BIN = "bin" ;
  private final String SHARE = "share" ;
  private final String COMMAND = "nmap" ;
  
  /**
   * Constructs an instance of NMapProperties and looks in the environment
   * properties for NMAP_HOME.  If it is not found, the path is initialized
   * to null and the API assumes that you will set it manually. See setPath().
   */
  public NMapProperties() {
    String path = System.getenv().get( "NMAP_HOME" ) ;
    if(  path != null && path.length() > 0 ) {
      pathToNMap = path ;
    }
  }
  
  /**
   * Contructs and instance of NMapProperties using the path passed.
   * @param path
   */
  public NMapProperties( String path ) {
    pathToNMap = path ;
  }
  
  /**
   * Returns the current path.
   * @return
   */
  public String getPath() {
    return pathToNMap ;
  }
  
  /**
   * Sets the path the bin dir where nmap can be found.  This is also the path
   * to the share dir which contains important files for nmap.
   * <p>
   * For example, if the nmap bin dir is in /usr/local/share/bin/nmap the
   * path you would set into this method is /usr/local/share .
   * @param pathToBinDir - /the/path/to/nmapbindir.
   */
  public void setPath( String pathToBinDir ) {
    pathToNMap = pathToBinDir ;
  }
  
  /**
   * Returns the expected location of the share dir relative to the path set
   * or passed in at construction time. The value returned by this method is 
   * equivalent to that path variable +  filesystem dependent separator + bin .
   * @return
   */
  public String getBinDir() { 
    return pathToNMap + File.separator + BIN ;
  }
  
  /**
   * Returns the expected location of the share dir relative to the path set
   * or passed in at construction time.  The value returned by this method is 
   * equivalent to the path variable + filesystem dependent separator + share.
   * @return
   */
  public String getShareDir() {
    return pathToNMap + File.separator + SHARE + File.separator + "nmap" ;
  }
   
  /**
   * Returns the system's  os.name property.
   * @return
   */
  private String getOS() {
    return System.getProperty( "os.name" ) ;
  }
  
  /**
   * This returns the full path to the nmap version to be executed.
   * @return
   */
  public String getFullyFormattedCommand() {
	
    StringBuffer command = new StringBuffer() ;
   
    if ( getOS().toLowerCase().contains( "windows" ) ){
    	command.append( pathToNMap ) ;
        command.append( File.separator ) ;
        command.append( COMMAND ) ;
    	command.append( ".exe" ) ;
    } 
    else { //Linux or MacOsX case
    	command.append( getBinDir() ) ;
    	command.append( File.separator ) ;
    	command.append( COMMAND ) ;
    	command.append( " " ) ;
    	command.append( Flag.DATADIR ) ;
    	command.append( " " ) ;
    	command.append( getShareDir() ) ;
    }
    
    return command.toString() ;
  }
}
