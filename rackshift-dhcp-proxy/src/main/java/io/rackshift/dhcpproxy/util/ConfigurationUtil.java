package io.rackshift.dhcpproxy.util;

import io.rackshift.dhcpproxy.config.MongoConfig;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.Optional;

public class ConfigurationUtil {
    private static PropertiesConfiguration config;

    public static void init() {

        try {
            config = new PropertiesConfiguration("/opt/rackshift/conf/rackshift.properties");
            MongoConfig.config(config);
        } catch (ConfigurationException e) {
            System.out.println("Cannot find rackshift.properties");
            throw new RuntimeException();
        }
    }

    public static String getConfig(String key, String defaultS) {
        return Optional.ofNullable(config.getString(key)).orElse(defaultS);
    }

}
