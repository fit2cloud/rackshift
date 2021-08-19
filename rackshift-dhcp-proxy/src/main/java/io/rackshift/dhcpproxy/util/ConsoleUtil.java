package io.rackshift.dhcpproxy.util;

import io.rackshift.dhcpproxy.constants.ConfigConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleUtil {
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-DD hh:mm:ss");

    public static void log(String msg) {
        ConsoleUtil.log(ConfigConstants.APPLICATION_NAME + " " + dtf.format(LocalDateTime.now()) + " " + msg);
    }
}
