package io.rackshift.dhcpproxy.util;

import io.rackshift.dhcpproxy.constants.ConfigConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleUtil {
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");

    public static void log(String msg) {
        System.out.println(ConfigConstants.APPLICATION_NAME + " " + dtf.format(LocalDateTime.now()) + " " + msg);
    }
}
