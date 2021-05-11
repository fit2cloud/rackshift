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
package org.nmap4j.core.scans;

import org.nmap4j.core.flags.ArgumentProperties;
import org.nmap4j.core.flags.Flag;
import org.nmap4j.core.nmap.ExecutionResults;
import org.nmap4j.core.nmap.NMapExecutionException;
import org.nmap4j.core.nmap.NMapInitializationException;
import org.nmap4j.core.nmap.NMapProperties;

/**
 * Parent interface of all Scan implentations.  Allows you to customize the
 * scan behavior if for any reason the BaseScan doesn't meet your needs.
 * 
 * @author jsvede
 *
 */
public interface IScan {
  
  public enum TimingFlag { PARANOID, SNEAKY, POLITE, NORMAL, AGGRESSIVE, INSANE } ;
  
  public enum OutputType { XML, GREPPABLE, SCRIPT_KIDDIE, NORMAL  } ;

  /**
   * Sets the scan timing flag.
   * 
   * @param tf
   */
  public void setTiming( TimingFlag tf ) ;
  
  /**
   * Adds a single port to the scan spec; adds the implied -p flag.  You can
   * call this method repetitively to add ports.
   * 
   * @param port
   */
  public void addPort( int port ) ;
  
  /**
   * Allows you to add ports in a block; this can also be called repetitively.
   * @param ports
   */
  public void addPorts( int[] ports ) ;
  
  /**
   * Add a single host to the list of hosts.  Additive.
   * 
   * @param host
   */
  public void includeHost( String host ) ;
  
  /**
   * Add the the hosts from the array. Additive.
   * 
   * @param hosts
   */
  public void includeHosts( String[] hosts ) ;
  
  /**
   * Add a single host to the list of excluded hosts. Additive.
   * 
   * @param host
   */
  public void excludeHost( String host ) ;
  
  /**
   * Add the array of hosts to the list of hosts that are excluded. Additive.
   * 
   * @param hosts
   */
  public void excludeHosts( String[] hosts ) ;
  
  /**
   * Removes the specified host from the list of included hosts.
   * 
   * @param host
   */
  public void removeIncludeHost( String host ) ;
  
  /**
   * Removes all the hosts in the array from the list of included hosts.  
   * Should not throw any exceptions if a host in the array is not in the 
   * current list.
   * 
   * @param hosts
   */
  public void removeIncludeHosts( String[] hosts ) ;
  
  /**
   * Remove one host from the list of excluded hosts.
   * 
   * @param host
   */
  public void removeExcludeHost( String host ) ;
  
  /**
   * Remove the list of hosts from the list of hosts that should be excluded
   * from a scan.
   * 
   * @param hosts
   */
  public void removeExcludeHosts( String[] hosts ) ;
  
  
  /**
   * Sets the output type flag accordingly and also sets the filename.  This
   * method should be used to override the default behavior, writing XML to the 
   * std out.
   * 
   * @param ot
   */
  public void setOutputType( OutputType ot, String fileName ) ;
  
  /**
   * Executes a scan and blocks while scan runs.  If your scan is long 
   * running consider using the executeAsynchronousExecute() method.
   * 
   * @return
   * @throws NMapInitializationException 
   */
  public ExecutionResults executeScan() 
    throws ParameterValidationFailureException, 
           NMapExecutionException, NMapInitializationException ;
  
  /**
   * Execute a scan asynchronously; you must pass in a callback in order
   * for this to work.
   * 
   * @param isc
   */
  public void executeAsynchronousScan( IScanCallback isc )  
    throws ParameterValidationFailureException, 
           NMapExecutionException ;
  
  /**
   * Allows for a controlled way to vet a scan configuration.  Allows API users
   * to verify the right combination of flags, the right values for things like
   * ports or hosts (for example, allows users to prevent hosts from being
   * scanned, etc).
   * 
   * @param isv
   * @throws ParameterValidationFailureException
   */
  public void setScanValidator( IScanValidator isv ) ;
  
  /**
   * Specifies the path in which to look for the nmap binaries and share 
   * directory.  If this is not set, the API will try to use the environmental
   * variable NMAP_HOME.
   * @param path
   */
  public void setNMapPath( String path ) ;
  
  /**
   * Adds the specified flag to the underlying ArgumentProperties object. 
   */
  public void addFlag( Flag flag ) ;
  
  /**
   * Removes the specified flag from the underlying ArgumentProperties object.
   */
  public void removeFlag( Flag flag ) ;
  
  /**
   * This provides access to the member variable for the ArgumentProperties.
   * @return
   */
  public ArgumentProperties getArgumentProperties() ;
  
  /**
   * This provides access to the member variable for the NMapProperties.
   * @return
   */
  public NMapProperties getNMapProperties() ;
}
