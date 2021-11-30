package io.rackshift.dhcpproxy.util;

import io.rackshift.dhcpproxy.constants.ConfigConstants;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.Iterator;
import java.util.Optional;

public class ConfigurationUtil {
    private static PropertiesConfiguration config;

    static {

        try {
            config = new PropertiesConfiguration(ConfigConstants.CONFIG_FILE);
            Iterator<String> keys = config.getKeys();
            while (keys.hasNext()) {
                String k = keys.next();
                if (k.equalsIgnoreCase("spring.datasource.password")) {
                    ConsoleUtil.log("key: " + k + " value: ******");
                } else {
                    ConsoleUtil.log("key: " + k + " value: " + config.getString(k));
                }
            }
        } catch (ConfigurationException e) {
            ConsoleUtil.log("Cannot find " + ConfigConstants.CONFIG_FILE);
            throw new RuntimeException();
        }
    }

    public static String getConfig(String key, String defaultS) {
        return Optional.ofNullable(config.getString(key)).orElse(defaultS);
    }

    public static String[] getConfigs(String key, String defaultS) {
        return Optional.ofNullable(config.getString(key).split(",")).orElse(new String[]{defaultS});
    }

}
