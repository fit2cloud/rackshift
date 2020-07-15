package io.rackshift.config;

import io.rackshift.metal.sdk.MetalPlugin;
import io.rackshift.metal.sdk.util.CloudProviderManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class PluginConfig {

    @Bean(initMethod = "init")
    public CloudProviderManager metalProviderManager() {
        String pluginLocation = "/opt/share/plugins";
        File file = new File(pluginLocation);
        if (!file.exists()) {
            pluginLocation = "/opt/rackshift/plugins";
        }
        return new CloudProviderManager(MetalPlugin.class, pluginLocation, "io.rackshift");
    }
}
