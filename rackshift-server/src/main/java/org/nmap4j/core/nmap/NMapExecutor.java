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
package org.nmap4j.core.nmap ;

import java.io.BufferedReader ;
import java.io.IOException ;
import java.io.InputStream ;
import java.io.InputStreamReader ;
import java.io.OutputStream ;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.nmap4j.core.flags.ArgumentProperties ;

/**
 * A simple class that encapsulates executing NMap.
 * <p>
 * This class executes nmap synchronously.
 * 
 * @author jsvede
 * 
 */
public class NMapExecutor {

    private ArgumentProperties nmapArguments ;
    private NMapProperties nmapProperties ;

    /**
     * Constructs an instance based on the ArgumentProperties and the
     * NMapProperties passed in. These become the basis for the subsequent
     * execution of NMap.
     * 
     * @param argProps
     * @param nmapProps
     * @throws NMapInitializationException
     */
    public NMapExecutor( ArgumentProperties argProps, NMapProperties nmapProps )
            throws NMapInitializationException {
        nmapArguments = argProps ;
        nmapProperties = nmapProps ;
        if ( nmapArguments == null || nmapProperties == null ) {
            throw new NMapInitializationException(
               "You cannot instantiate "
               + "an NMapExecutor with nulls in either argument. Please "
               + "refer to the documentation if you aren't sure how to proceed." ) ;
        }
        if ( nmapProps.getPath() == null
                || ( nmapProps.getPath() != null && nmapProps.getPath()
                        .length() <= 0 ) ) {
            throw new NMapInitializationException(
                    "the NMAP_HOME variable is not set "
                            + "or you did not set this path." ) ;
        }
    }

    /**
     * Returns the system's os.name property.
     * 
     * @return
     */
    private String getOS() {
        return System.getProperty( "os.name" ) ;
    }

    /**
     * Get the nmap command as a StringBuffer.
     * 
     * @return
     */
    private StringBuffer getCommand() {

        StringBuffer fullCommand = new StringBuffer() ;
        fullCommand.append( nmapProperties.getFullyFormattedCommand() ) ;
        fullCommand.append( " " ) ;
        fullCommand.append( nmapArguments.getFlags() ) ;

        return fullCommand ;
    }

    /**
     * This method attempts to execute NMap using the properties supplied when
     * this object was constructed.
     * <p>
     * This method can throw an NMapExecutionException which will be a wrapper
     * around an IO Exception.
     * 
     * @return
     * @throws NMapExecutionException
     */
    public ExecutionResults execute() throws NMapExecutionException {
        StringBuffer command = getCommand() ;
        ExecutionResults results = new ExecutionResults() ;

        try {
            results.setExecutedCommand( command.toString() ) ;
            Process process = Runtime.getRuntime().exec( command.toString() ) ;

            CompletableFuture<String> errorPromise = convertStream( process.getErrorStream() ) ;
            CompletableFuture<String> outputPromise = convertStream( process.getInputStream() );
            
            results.setOutput(outputPromise.get()) ;
            results.setErrors(errorPromise.get()) ;

        } catch ( IOException | InterruptedException | ExecutionException e ) {
            throw new NMapExecutionException( e.getMessage(), e ) ;
		}

        return results ;
    }

    /**
     * Converts the given InputStream to a String. This is how the streams from
     * executing NMap are converted and later stored in the ExecutionResults.
     * 
     * @param is
     * @return promise of converted String
     * @throws IOException
     */
    private CompletableFuture<String> convertStream( InputStream is ) throws IOException {
    	CompletableFuture<String> promise = CompletableFuture.supplyAsync(() -> {
            String output ;
            StringBuffer outputBuffer = new StringBuffer() ;
            BufferedReader streamReader = new BufferedReader(
                    new InputStreamReader( is ) ) ;
            try {
				while ( ( output = streamReader.readLine() ) != null ) {
				    outputBuffer.append( output ) ;
				    outputBuffer.append( "\n" ) ;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            return outputBuffer.toString();
        });
        
        return promise;
    }

    public String toString() {
        return getCommand().toString() ;
    }

}
