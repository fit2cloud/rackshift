package io.rackshift.config;

import io.rackshift.constants.PluginConstants;
import io.rackshift.metal.sdk.IMetalProvider;
import io.rackshift.metal.sdk.MetalPlugin;
import io.rackshift.metal.sdk.util.CloudProviderManager;
import io.rackshift.mybatis.domain.BareMetal;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Configuration
public class PluginConfig {

    private List<MetalPlugin> plugins = new LinkedList<>();

    @Bean(initMethod = "init")
    public CloudProviderManager metalProviderManager() {
        String pluginLocation = "/opt/share/plugins";
        File file = new File(pluginLocation);
        if (!file.exists()) {
            pluginLocation = "/opt/rackshift/plugins";
        }
        return new CloudProviderManager(MetalPlugin.class, pluginLocation, "io.rackshift");
    }

    /**
     * 通过 品牌型号，优先型号来匹配插件
     *
     * @param bareMetal
     * @return
     */
    public IMetalProvider getPlugin(BareMetal bareMetal) {
        IMetalProvider finalType = null;
        IMetalProvider validType = null;
        for (Object provider : metalProviderManager().getCloudProviders()) {
            validType = (IMetalProvider) provider;
            if (StringUtils.isNotBlank(bareMetal.getMachineBrand()) && validType.getSupportedBrands().contains(bareMetal.getMachineBrand().trim())) {
                if (StringUtils.isNotBlank(bareMetal.getMachineModel()) && validType.getSupportedModels().contains(bareMetal.getMachineModel().trim())) {
                    finalType = validType;
                    break;
                }
            }
        }

        finalType = Optional.ofNullable(finalType).orElse(validType);
        if (finalType == null)
            throw new RuntimeException("不支持的机型！" + bareMetal.getMachineBrand());
        return finalType;
    }

    /**
     * 只通过品牌来匹配插件
     *
     * @param brand
     * @return
     */
    public IMetalProvider getPluginByBrand(String brand) {
        IMetalProvider validType = null;
        for (Object provider : metalProviderManager().getCloudProviders()) {
            validType = (IMetalProvider) provider;
            if (StringUtils.isNotBlank(brand) && validType.getSupportedBrands().contains(brand.trim())) {
                return validType;
            }
        }
        throw new RuntimeException("不支持的机型！" + brand);
    }
}
