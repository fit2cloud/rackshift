package org.nmap4j.valid;

import com.google.common.net.InternetDomainName;

import java.io.File;
import java.util.regex.Pattern;

/**
 * nmap can scan over hostname, single ip, subnet, or using a file input
 */
public class HostsInputValidator {
    private final static String IP_REGEX = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";

    private final static String IP_CIDR = "(\\/([0-9]|[1-2][0-9]|3[0-2]))";

    public boolean valid(String host) {
        return InternetDomainName.isValid(host) || isIp(host) || isSubnet(host) || isFile(host);
    }


    private boolean isIp(final String ip) {
        final Pattern PATTERN = Pattern.compile(IP_REGEX + "$");
        return PATTERN.matcher(ip).matches();
    }

    private boolean isSubnet(final String subnet) {
        final Pattern PATTERN = Pattern.compile(IP_REGEX + IP_CIDR);
        return PATTERN.matcher(subnet).matches();
    }

    private boolean isFile(String host) {
        return new File(host).exists();
    }

}
