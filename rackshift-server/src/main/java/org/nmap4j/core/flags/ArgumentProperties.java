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
package org.nmap4j.core.flags;

import java.util.HashSet ;
import java.util.Iterator ;
import java.util.LinkedHashMap ;
import java.util.Set ;

/**
 * Instances of this class are used to manage the flags for a given 
 * execution.
 * <p>
 * Flags are ordered, so the order in which you add them is retained.
 * <p>
 * This class also manages hosts (included and excluded) as well, even
 * though the hosts are not explicitly flagged the way other things are.
 * <p>
 * Hosts are not ordered when stored, as nmap is agnostic in this regard.
 * 
 */
public class ArgumentProperties {

  private LinkedHashMap<String, String> flagMap ;
  
  private HashSet<String> includeHosts ;
  private HashSet<String> excludeHosts ;
  

  
  public ArgumentProperties() {
    flagMap = new LinkedHashMap<String, String>() ;
    includeHosts = new HashSet<String>() ;
    excludeHosts = new HashSet<String>() ;
  }
  
  public void addFlag( Flag f ) {
    addFlag( f.toString() ) ;
  }
  
  public void addFlag( Flag f, String value ) {
    addFlag( f.toString(), value ) ;
  }
  
  public void addFlag( String singleFlag ) {
    addFlag( singleFlag, null );
  }
  
  public void addFlag( String flag, String arg ) {
    /* If the flag exists and the arg isn't null, update the args by appending
     * the new flags.
     * if the flag exists and the arg is null, add the flag and the null. (some
     * flags are additive like that, -v for example)
     * If the flag doesn't exist add it and the arg.
     */
    if( flagMap.keySet().contains( flag ) && 
        flagMap.get( flag ) != null  )  {
      StringBuffer tempFlags = new StringBuffer() ;
      tempFlags.append(  flagMap.get( flag ) ) ;
      tempFlags.append(  "," ) ;
      tempFlags.append( arg ) ;
      flagMap.remove(  flag ) ;
      flagMap.put( flag, tempFlags.toString() ) ;
    } else {
      flagMap.put( flag, arg );      
    }
  }
  
  public void replaceFlag( Flag f, String value ) {
    if( flagMap.get(  f.toString() ) != null ) {
      removeFlag(  f ) ;
    }
    flagMap.put(  f.toString(), value ) ;
  }
  
  public void removeFlag( Flag f ) {
    flagMap.remove( f.toString() ) ;
  }
  
  public LinkedHashMap<String, String> getFlagMap() {
    return flagMap ;
  }
  
  public void setFlagMap( LinkedHashMap<String, String> newMap ) {
    flagMap = newMap ;
  }
   
  public String getFlags() {
    StringBuffer flags = new StringBuffer() ;
    Set<String> mapKeys = flagMap.keySet() ;
    Iterator<String> keysIt = mapKeys.iterator() ;
    while( keysIt.hasNext() ) {
      String keyValue = keysIt.next() ;
      String argumentForKey = flagMap.get( keyValue ) ;
      flags.append( keyValue ) ;
      if( argumentForKey != null ) {
        flags.append( " " ) ;
        flags.append( argumentForKey ) ;
      }
      flags.append( " " ) ;
    }
    
    // excluded hosts are comma delimited
    if( excludeHosts != null && excludeHosts.size() > 0 ) {
      flags.append( Flag.EXCLUDE_HOSTS.toString() ) ;
      flags.append( " " ) ;
      flags.append( convertSetToString( excludeHosts, "," ) ) ;
    }
    
    // hosts don't have an argument and go last - this should really
    // never be null, but that existence of hosts should be checked in
    // a validator.
    if( includeHosts != null && includeHosts.size() > 0 ) {
      flags.append( " " ) ;
      flags.append( convertSetToString( includeHosts, " " ) ) ;
    }
    return flags.toString();
  }
  
  public void addIncludedHost( String host ) {
    if( !includeHosts.contains(  host ) ) {
      includeHosts.add( host ) ;
    }
  }
  
  public void removeIncludedHost( String host ) {
    if( includeHosts.contains(  host ) ) {
      includeHosts.remove( host ) ;
    }
  }
  
  public String getIncludedHostsAsString() {
    return convertSetToString( includeHosts, " " ) ;
  }
  
  public Set<String> getIncludedHosts() {
	  return includeHosts ;
  }
  
  public void addExcludedHost( String host ) {
    if( !excludeHosts.contains( host ) ) {
      excludeHosts.add( host ) ;
    }
  }

  public void removeExcludedHost( String host ) {
    if( excludeHosts.contains( host ) ) {
      excludeHosts.remove( host ) ;
    }
  }

  public Set<String> getExcludeHost() {
	  return excludeHosts ;
  }
  
  public String getExcludedHostsAsString() {
    return convertSetToString( excludeHosts,"," ) ;
  }
  
  public void clearFlags() {
	  flagMap.clear() ;
  }
  
  private String convertSetToString( Set<String> hosts, String delimiter ) {
    StringBuffer hostsBuffer = new StringBuffer() ;
    Iterator<String> hostsIt = hosts.iterator() ;
    while( hostsIt.hasNext() ) {
      hostsBuffer.append( hostsIt.next() ) ;
      hostsBuffer.append( delimiter ) ;
    }
    return hostsBuffer.toString() ;
  }
}
