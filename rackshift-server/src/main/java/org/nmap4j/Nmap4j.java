package org.nmap4j;

import org.nmap4j.core.flags.ArgumentProperties;
import org.nmap4j.core.nmap.ExecutionResults;
import org.nmap4j.core.nmap.NMapExecutionException;
import org.nmap4j.core.nmap.NMapExecutor;
import org.nmap4j.core.nmap.NMapInitializationException;
import org.nmap4j.core.nmap.NMapProperties;
import org.nmap4j.data.NMapRun;
import org.nmap4j.parser.OnePassParser;
import org.nmap4j.valid.HostsInputValidator;


/**
 * This is the simplified way to execute and parse Nmap output.  This is the
 * easiest way to run Nmap from this API.  Use of this class requires a little
 * more knowledge of Nmap's flags in order to work.
 * <p>
 * Here is an example of how to use this class:
 * <code>
 * Nmap4j nmap4j = new Nmap4j( "/usr/local" ) ;
 * nmap4j.includeHosts( "192.168.1.1-255" ) ;
 * nmap4j.excludeHosts( "192.168.1.110" ) ;
 * nmap4j.addFlags( "-T3 -oX - -O -sV" ) ;
 * nmap4j.execute() ;
 * if( !nma4j.hasError() ) {
 * NMapRun nmapRun = nmap4j.getResults() ;
 * } else {
 * System.out.println( nmap4j.getExecutionResults().getErrors() ) ;
 * }
 * </code>
 * <p>
 * This block would need a try/catch because the execute() method throws two
 * different exceptions.
 *
 * @author jsvede
 */
public class Nmap4j implements INmap4j {

    private NMapProperties nmapProperties;
    private ArgumentProperties flags;
    private NMapExecutor nmapExecutor;
    private ExecutionResults results;
    private HostsInputValidator validator;

    /**
     * Constructs this object with the path specified.  This path needs to be
     * the path to your Nmap binary.  On many systems this will be something
     * like "/usr/local".  Additionally, this will also be the path to the
     * data dir required by Nmap.
     *
     * @param path
     */
    public Nmap4j(String path) {
        nmapProperties = new NMapProperties(path);
        flags = new ArgumentProperties();
        validator = new HostsInputValidator();
    }

    /**
     * Executes the nmap scan with the parameters set.  You should have
     * called addFlags() with appropriate Nmap flags prior to executing the
     * scan.
     *
     * @throws NMapInitializationException
     * @throws NMapExecutionException
     */
    public void execute() throws NMapInitializationException, NMapExecutionException {
        nmapExecutor = new NMapExecutor(flags, nmapProperties);
        results = nmapExecutor.execute();
    }

    /**
     * Add the appropriate flags to your scan.  Call this method with all the
     * flags you will want.  For example, if you want to scan for hosts, OS
     * information and service information you would pass "-sV -O -T4". This
     * method will append "-oX -" if you did not supply it.
     *
     * @param flagSet
     */
    public void addFlags(String flagSet) {
        StringBuilder sb = new StringBuilder();
        sb.append(flagSet);
        if (!flagSet.contains("-oX")) {
            sb.append(" -oX -");
        }
        flags.addFlag(sb.toString());
    }

    /**
     * Add a list of space delimited hosts that you want to scan.  This
     * list conforms to the requirements that Nmap sets forth.
     *
     * @param hosts
     */
    public void includeHosts(String hosts) {
        if (!validator.valid(hosts)) {
            throw new RuntimeException("Non legal hosts parameter");
        }
        flags.addIncludedHost(hosts);
    }

    /**
     * Add a list of space delimited hosts to exclude.  Usually this is used
     * when you specify a large included host list.  This allows you specify
     * broad ranges host addresses and exclude some hosts within that range.
     *
     * @param hosts
     */
    public void excludeHosts(String hosts) {
        flags.addExcludedHost(hosts);
    }

    /**
     * Returns the raw output of the execution.
     *
     * @return
     */
    public String getOutput() {
        return results.getOutput();
    }

    /**
     * This method returns an object tree representing the XML nodes.
     *
     * @return
     */
    public NMapRun getResult() {
        OnePassParser parser = new OnePassParser();
        NMapRun nmapRun = parser.parse(results.getOutput(), OnePassParser.STRING_INPUT);
        return nmapRun;
    }

    /**
     * Checks the output for the word "ERROR" as Nmap will usually produce an
     * error message that starts with ERROR though there are other scenarios. If
     * the call to getResult() fails check the error output.
     *
     * @return
     */
    public boolean hasError() {
        return results.getErrors().contains("ERROR");
    }

    /**
     * Use this method to get the raw results of the execution.  The
     * ExecutionResults contains the raw output, the errors and the
     * command that was executed.
     *
     * @return
     */
    public ExecutionResults getExecutionResults() {
        return results;
    }

}
