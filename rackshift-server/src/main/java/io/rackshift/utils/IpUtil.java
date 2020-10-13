package io.rackshift.utils;

import org.apache.commons.net.util.SubnetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IpUtil {
    public static List<String> getIpRange(String startIp, String endIp, String mask) {
        if(startIp != null && startIp.equals(endIp) && "255.255.255.255".equals(mask)) {
            return new ArrayList<>(Collections.singletonList(startIp));
        }
        List<String> result = new ArrayList<>();
        SubnetUtils utils = new SubnetUtils(startIp, mask);
        SubnetUtils.SubnetInfo info = utils.getInfo();
        String[] allIps = info.getAllAddresses();
        for (int i = 0; i < allIps.length; i++) {
            if (ipInRange(allIps[i], startIp, endIp)) {
                result.add(allIps[i]);
            }
        }
        return result;
    }

    public static Boolean ipInRange(String ip, String startIp, String endIp) {
        String ipSection = startIp + "-" + endIp;
        ip = ip.trim();
        final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
        final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
        if (!ipSection.matches(REGX_IPB) || !ip.matches(REGX_IP))
            return false;
        int idx = ipSection.indexOf('-');
        String[] sips = ipSection.substring(0, idx).split("\\.");
        String[] sipe = ipSection.substring(idx + 1).split("\\.");
        String[] sipt = ip.split("\\.");
        long ips = 0L, ipe = 0L, ipt = 0L;
        for (int i = 0; i < 4; ++i) {
            ips = ips << 8 | Integer.parseInt(sips[i]);
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);
        }
        if (ips > ipe) {
            long t = ips;
            ips = ipe;
            ipe = t;
        }
        return ips <= ipt && ipt <= ipe;
    }

    public static long ipToInt(String ip) {
        long ipNumbers = 0;
        String[] split = ip.split("\\.");
        for (int i = 0; i < 4; i++) {
            ipNumbers += Long.parseLong(split[i]) << (24 - (8 * i));
        }
        return ipNumbers;
    }
}
