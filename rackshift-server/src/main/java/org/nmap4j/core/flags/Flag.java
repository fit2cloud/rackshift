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

/**
 * This enum contains all the publically available flags for NMap as 
 * Java constants.  It is not a requirement with NMap4J to use these flags,
 * you are free to pass flags as Strings.
 * <p>
 * To see the complete list of flags, run nmap with no arguments from the 
 * command line.
 * <p>
 * @author jsvede
 *
 */
public enum Flag {

  INPUT_FILENAME( "-iL", "<inputfilename>: Input from list of hosts/network" ), 
  RANDOM_TARGETS( "-iR", "<num hosts>: Choose random targets" ),  
  EXCLUDE_HOSTS( "--exclude", "<host1[,host2][,host3],...>: Exclude hosts/networks" ), 
  EXCLUDE_FILE( "--excludefile", "<exclude_file>: Exclude list from file" ), 
  LIST_SCAN( "-sL", "List Scan - simply list targets to scan" ),
  PING_SCAN( "-sP", "Ping Scan - go no further than determining if host is online" ),
  TREAT_HOSTS_AS_ONLINE( "-PN", "Treat all hosts as online -- skip host discovery" ), 
  TCP_SYN_DISCOVERY("-PS", "[portlist]: TCP SYN discovery to given ports"),
  ACK_DISCOVERY("-PA", "[portlist]: ACK discovery to given ports"),
  UDP_DISCOVERY("-PU", "[portlist]: UDP discovery to given ports"),
  SCTP_DISCOVERY("-PY","[portlist]: SCTP discovery to given ports"),
  ICMP_ECHO_DISCOVERY("-PE", "ICMP echo discovery probes"),
  TIMESTAMP_DISCOVERY("-PP", "ICMP echo, timestamp, and netmask request discovery probes"),
  NETMASK_REQUEST_DISCOVERY("-PM", "netmask request discovery probes"),
  PROTOCOL_PING( "-PO", "[protocol list]: IP Protocol Ping" ), 
  NEVER_DO_DNS( "-n", "Never do DNS resolution/Always resolve [default: sometimes]" ), 
  DNS_SERVERS( "--dns-servers", "<serv1[,serv2],...>: Specify custom DNS servers" ), 
  SYSTEM_DNS( "--system-dns", "Use OS's DNS resolver" ), 
  TRACEROUTE( "--traceroute", "Trace hop path to each host" ),
  HOST_SCAN( "-sn", "list host only, no services" ), 
  TCP_SYN_SCAN( "-sS", "TCP SYN scan" ),
  CONNECT_SCAN( "-sT", "Connect() scan" ),
  ACK_SCAN( "-sA", "ACK scan" ),
  WINDOW_SCAN( "-sW", "Window scan" ),
  MAIMON_SCAN( "-sM", "Maimon scan"),
  UDP_SCAN( "-sU", "UDP scan" ), 
  TCP_NULL_SCAN( "-sN", "TCP Null scan" ),
  FIN_SCAN( "-sF", "FIN scan" ),
  XMAS_SCAN( "-sX", "Xmas scan" ),
  SCANFLAGS( "--scanflags", "<flags>: Customize TCP scan flags" ), 
  IDLE_SCAN( "-sI", "<zombie host[:probeport]>: Idle scan"),
  SCTP_INIT_SCAN( "-sY", "SCTP INIT scan" ),
  COOKIE_ECHO_SCAN( "-sZ", "COOKIE-ECHO scan" ),
  IP_PROTOCOL_SCAN( "-sO", "IP protocol scan" ), 
  FTP_BOUNCE_SCAN( "-b", "<FTP relay host>: FTP bounce scan" ),
  PORT_SPEC( "-p", "<port ranges>: Only scan specified ports" ), 
  FAST_SCAN_MODE( "-F", "Fast mode - Scan fewer ports than the default scan" ),
  SCAN_PORTS_CONSECUTIVELY( "-r", "Scan ports consecutively - don't randomize" ), 
  TOP_PORTS( "--top-ports", "<number>: Scan <number> most common ports" ), 
  PORT_RATIO( "--port-ratio", "<ratio>: Scan ports more common than <ratio>" ), 
  SERVICE_VERSION( "-sV", "Probe open ports to determine service/version info" ), 
  VERSION_INTENSITY( "--version-intensity", "<level>: Set from 0 (light) to 9 (try all probes)" ), 
  VERSION_LIGHT( "--version-light", "Limit to most likely probes (intensity 2)" ),
  VERSION_ALL( "--version-all", "Try every single probe (intensity 9)" ),
  VERSION_TRACE( "--version-trace", "Show detailed version scan activity (for debugging)" ),
  SCRIPT_SC( "-sC", "equivalent to --script=default" ),
  SCRIPT( "--script", "--script=<Lua scripts>: <Lua scripts> is a comma separated list of directories, script-files or script-categories"), //
  SCRIPT_ARGS( "--script-args", "--script-args=<n1=v1,[n2=v2,...]>: provide arguments to scripts"),
  SCRIPT_TRACE( "--script-trace", "Show all data sent and received"),
  SCRIPT_UPDATEDB( "--script-updatedb", "Update the script database." ), 
  OS_DETECTION( "-O", "Enable OS detection"),
  OSSCAN_LIMIT( "--osscan-limit", "Limit OS detection to promising targets"),
  OSSCAN_GUESS( "--osscan-guess", "Guess OS more aggressively" ),
  PARANOID_TIMING( "-T0", "<0-5>: Set timing template (higher is faster)"), 
  SNEAKY_TIMING( "-T1", "<0-5>: Set timing template (higher is faster)"), 
  POLITE_TIMING( "-T2", "<0-5>: Set timing template (higher is faster)"), 
  NORMAL_TIMING( "-T3", "<0-5>: Set timing template (higher is faster)"), 
  AGGRESIVE_TIMING( "-T4", "<0-5>: Set timing template (higher is faster)"), 
  INSANE_TIMING( "-T5", "<0-5>: Set timing template (higher is faster)"), 
  PARALLEL_MIN_HOST_GROUP_SIZE( "--min-hostgroup", "<size>: Parallel host scan group sizes"),
  PARALLEL_MAX_HOST_GROUP_SIZE( "--max-hostgroup", "<size>: Parallel host scan group sizes"), 
  MIN_PROBE_PARALLELIZATION( "--min-parallelism", "<time>: Probe parallelization"), 
  MAX_PROBE_PARALLELIZATION( "--max-parallelism",  "<time>: Probe parallelization" ),
  MIN_RTT_TIMEOUT( "--min-rtt-timeout", "<time>: Specifies probe round trip time."),
  MAX_RTT_TIMEOUT( "--max-rtt-timeout", "<time>: Specifies probe round trip time."),
  INITIAL_RTT_TIMEOUT( "--initial-rtt-timeout",  "<time>: Specifies probe round trip time." ),
  MAX_RETRIES( "--max-retries", "<tries>: Caps number of port scan probe retransmissions." ),
  HOST_TIMEOUT( "--host-timeout",  "<time>: Give up on target after this long" ),
  SCAN_DELAY( "--scan-delay", "<time>: Adjust delay between probes" ),
  MAX_SCAN_DELAY( "--max-scan-delay", "<time>: Adjust delay between probes" ),
  MIN_RATE( "--min-rate", "<number>: Send packets no slower than <number> per second" ),
  MAX_RATE("--max-rate", "<number>: Send packets no faster than <number> per second"),
  FRAGMENT_PACKETS("-f", "fragment packets (optionally w/given MTU)" ),
  MTU("--mtu", "<val>: fragment packets (optionally w/given MTU)"),
  DECOY("-D", "<decoy1,decoy2[,ME],...>: Cloak a scan with decoys"),
  SPOOF_SOURCE_ADDRESS ("-S",  "<IP_Address>: Spoof source address"),
  INTERFACE("-e", "<iface>: Use specified interface"),
  SOURCE_PORT_G_FLAG("-g", "<portnum>: Use given port number"),
  SOURCE_PORT("--source-port", "<portnum>: Use given port number"),
  DATA_LENGTH("--data-length", "<num>: Append random data to sent packets"),
  IP_OPTIONS("--ip-options", "<options>: Send packets with specified ip options"),
  TIME_TO_LIVE("--ttl", "<val>: Set IP time-to-live field"),
  SPOOF_MAC_ADDRESS("--spoof-mac", "<mac address/prefix/vendor name>: Spoof your MAC address"),
  BADSUM("--badsum", "Send packets with a bogus TCP/UDP/SCTP checksum"),
  ADLER32("--adler32", "Use deprecated Adler32 instead of CRC32C for SCTP checksums"),
  NORMAL_OUTPUT("-oN", "<file>: Output scan in normal format to the given filename."),
  XML_OUTPUT("-oX", "<file>: Output scan in XML format to the given filename."),
  SCRIPT_KIDDIE_OUPUT("-oS", "<file>: Output scan in s|<rIpt kIddi3 format to the given filename."),
  GREPPABLE_OUTPUT("-oG", "<file>: Output scan in Grepable format to the given filename."),
  ALL_THREE_OUTPUT("-oA", "<basename>: Output in the three major formats at once"),
  VERBOSE("-v", "Increase verbosity level (use twice or more for greater effect)"),
  DEBUG_LEVEL("-d", "[level]: Set or increase debugging level (Up to 9 is meaningful)"),
  REASON("--reason", "Display the reason a port is in a particular state"),
  OPEN("--open", "Only show open (or possibly open) ports"),
  PACKET_TRACE("--packet-trace", "Show all packets sent and received"),
  PRINT_HOST_INTERFACE_LIST("--iflist"," Print host interfaces and routes (for debugging)"),
  LOG_ERRORS("--log-errors", "Log errors/warnings to the normal-format output file"),
  APPEND_OUTPUT("--append-output", "Append to rather than clobber specified output files"),
  RESUME("--resume", "<filename>: Resume an aborted scan"),
  STYLESHEET("--stylesheet", "<path/URL>: XSL stylesheet to transform XML output to HTML"),
  WEBXML("--webxml", "Reference stylesheet from Nmap.Org for more portable XML"),
  NO_STYLESHEET("--no-stylesheet", "Prevent associating of XSL stylesheet w/XML output"),
  IPV6("-6", "Enable IPv6 scanning"),
  A_FLAG("-A", "Enables OS detection and Version detection, Script scanning and Traceroute"),
  DATADIR("--datadir", "<dirname>: Specify custom Nmap data file location"),
  SEND_ETH("--send-eth", "Send using raw ethernet frames or IP packets"),
  SEND_IP("--send-ip", "Send using raw ethernet frames or IP packets"),
  PRIVILEGED("--privileged", "Assume that the user is fully privileged"),
  UNPRIVILEGED("--unprivileged", "Assume the user lacks raw socket privileges"),
  VERSION("-V", "Print version number"),
  HELP("-h", "Print this help summary page.");
  
  private String aFlag ;
  private String comment ;
  
  private Flag( String f ) {
    aFlag = f ;
    comment = "" ;
  }
  
  private Flag( String f, String c) {
    aFlag = f ;
    comment = c ;
  }
  
  public String toString() {
    return aFlag ;
  }

  public String getComment() {
    return comment ;
  }
}
