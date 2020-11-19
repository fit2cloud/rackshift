package io.rackshift.config;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;

@Configuration
public class CacheConfig {
    @Resource
    private CacheManager cacheManager;

    @Bean
    public CacheManager cacheManager() {
        CacheManager manager = CacheManager.create();
        return manager;
    }

    @Bean
    @DependsOn("cacheManager")
    public Cache cache() {
        CacheConfiguration configuration = new CacheConfiguration();
        configuration.setName("commonCache");
        //半天的生存时间
        configuration.internalSetTimeToLive(12 * 60 * 60 * 1000);
        configuration.setMaxBytesLocalHeap(200 * 10000l);
        Cache c = new Cache(configuration);
        cacheManager.addCache(c);
        return c;
    }
}
