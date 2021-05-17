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
package org.nmap4j.core.scans ;

import org.nmap4j.core.flags.ArgumentProperties ;
import org.nmap4j.core.flags.Flag ;
import org.nmap4j.core.nmap.ExecutionResults ;
import org.nmap4j.core.nmap.NMapExecutionException ;
import org.nmap4j.core.nmap.NMapExecutor ;
import org.nmap4j.core.nmap.NMapInitializationException ;
import org.nmap4j.core.nmap.NMapProperties ;

/**
 * This is the base class for all convenience scan classes.  The 
 * children of this class make it easy for users not familiar with NMap to 
 * create scans without having to understand very much about NMap.
 * <p>
 * The BaseScan object is the easiest way to build a custom scan specification.
 * If you are familiar with NMap and want to have more control over how scans
 * are executed, then this is the class to use.
 * <p>
 * See the Javadocs for IScan for details on each method.
 * 
 * @author jsvede
 *
 */
public class BaseScan implements IScan {

  protected ArgumentProperties argProps ;
  protected NMapProperties nmapProps ;
  
  private String executedCommand ;
  
  public BaseScan() {
	  nmapProps = new NMapProperties() ;
	  argProps  = new ArgumentProperties() ;
	  
	  argProps.addFlag( Flag.XML_OUTPUT, "-" ) ;
  }
  
  public BaseScan( String nmapPath ) {
	  this() ;
	  nmapProps.setPath( nmapPath ) ;
  }

  @Override
  public void excludeHost( String host ) {
    if ( host != null ) {
      argProps.addExcludedHost( host ) ;
    }
  }

  @Override
  public void excludeHosts( String[] hosts ) {
    if ( hosts != null && hosts.length > 0 ) {
      for (String h : hosts) {
        excludeHost( h ) ;
      }
    }
  }

  @Override
  public void includeHost( String host ) {
    if( host != null && host.length() > 0 ) {
      argProps.addIncludedHost( host ) ;
    }
  }

  @Override
  public void includeHosts( String[] hosts ) {
    if( hosts != null && hosts.length > 0 ) {
      for( String h : hosts ) {
        includeHost( h ) ;
      }
    }
  }

  @Override
  public void addPort( int port ) {
    argProps.addFlag( Flag.PORT_SPEC, Integer.toString( port ) ) ;
  }

  @Override
  public void addPorts( int[] ports ) {
    if( ports != null && ports.length > 0 ) {
      for( int p : ports ) {
        argProps.addFlag( Flag.PORT_SPEC, Integer.toString( p ) ) ;
      }
    }
  }

  @Override
  public void executeAsynchronousScan( IScanCallback isc )
      throws ParameterValidationFailureException, NMapExecutionException {
    NMapExecutorThread nmapExecThread = new NMapExecutorThread( argProps, nmapProps ) ;
    nmapExecThread.setCallback( isc ) ;
    Thread aThread = new Thread( nmapExecThread ) ;
    aThread.start();
  }

  @Override
  public ExecutionResults executeScan() throws ParameterValidationFailureException,
      NMapExecutionException, NMapInitializationException {
    NMapExecutor executor = new NMapExecutor( argProps, nmapProps ) ;
    ExecutionResults results = executor.execute() ;
    executedCommand = executor.toString() ;
    return results ;
  }

  @Override
  public void removeExcludeHost( String host ) {
    if( host != null && host.length() > 0 ) {
      argProps.removeExcludedHost( host ) ;
    }
  }

  @Override
  public void removeExcludeHosts( String[] hosts ) {
    if( hosts != null && hosts.length > 0 ) {
      for( String h : hosts ) {
        removeExcludeHost( h ) ;
      }
    }
  }

  @Override
  public void removeIncludeHost( String host ) {
    if( host != null && host.length() > 0 ) {
      argProps.removeIncludedHost( host ) ;
    }
  }

  @Override
  public void removeIncludeHosts( String[] hosts ) {
    if( hosts != null && hosts.length> 0 ) {
      for( String h : hosts ) {
        removeIncludeHost( h ) ;
      }
    }
  }

  @Override
  public void setOutputType( OutputType ot, String fName ) {
    String fileName  = fName ;
    if( fName == null || (fName != null && fName.length() <= 0 ) ) {
      fileName = "-" ;
    }
    
    switch( ot ) {
    case GREPPABLE:
      argProps.addFlag( Flag.GREPPABLE_OUTPUT, fileName ) ;
      break;
    case SCRIPT_KIDDIE:
      argProps.addFlag( Flag.SCRIPT_KIDDIE_OUPUT, fileName ) ;
      break ;
    case XML:
      argProps.addFlag( Flag.XML_OUTPUT, fileName ) ;
      break ;
    case NORMAL:
      argProps.addFlag( Flag.NORMAL_OUTPUT, fileName ) ;
      break ;
    default:
      argProps.addFlag( Flag.XML_OUTPUT, fileName ) ;
      break ;
    }
  }

  @Override
  public void setScanValidator( IScanValidator isv ) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setTiming( TimingFlag tf ) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setNMapPath( String path ) {
    nmapProps.setPath( path ) ;
  }

   @Override
   public void addFlag(Flag flag) {
     argProps.addFlag( flag ) ;
   }
   
   @Override
   public void removeFlag(Flag flag) {
   	 argProps.removeFlag( flag ) ;
   }

	@Override
	public ArgumentProperties getArgumentProperties() {
		return argProps ;
	}

	@Override
	public NMapProperties getNMapProperties() {
		return nmapProps ;
	}
   
}
