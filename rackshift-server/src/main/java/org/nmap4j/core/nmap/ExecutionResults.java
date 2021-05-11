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

/**
 * This class contains the various outputs from an execution of nmap. This
 * includes the stdout and stderr.
 * <p>
 * In general it's not entirely possible for this framework to detect or handle
 * every failure. Therefore it's always best to check the contents of stderr
 * before wondering why there are no results.
 * 
 * @author jonsvede
 * 
 */
public class ExecutionResults {

	private String errors;
	private String output;
	private String executedCommand;

	public ExecutionResults() {}

	/**
	 * Create a new ExecutionResults object from the std out and std err
	 * from the nmap execution.
	 * 
	 * @param err
	 * @param out
	 */
	public ExecutionResults(String err, String out) {
		errors = err;
		output = out;
	}

	/**
	 * Returns the error stream from the nmap execution.
	 * @return
	 */
	public String getErrors() {
		return errors;
	}

	/**
	 * Sets the value for the error stream.
	 * 
	 * @param errors
	 */
	public void setErrors(String errors) {
		this.errors = errors;
	}

	/**
	 * Returns the value from the output stream from the NMap execution.
	 * @return
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * Sets the value for the std out.
	 * @param output
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * Returns true if the errors String is not null and it's length is
	 * greater than 0.
	 * @return
	 */
	public boolean hasErrors() {
		if (errors != null && errors.length() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the command executed to collect these outputs.
	 * @return
	 */
	public String getExecutedCommand() {
		return executedCommand;
	}

	/**
	 * Used to set the executed command.
	 * @param command
	 */
	public void setExecutedCommand(String command) {
		executedCommand = command;
	}

}
