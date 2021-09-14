package io.rackshift.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TaskConfig {
    @Value("${api.server.url}")
    private String apiServer;
    @Value("${api.server.port}")
    private String apiServerPort;

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        return scheduler;
    }

    /**
     * 用于渲染参数的一些默认选项
     *
     * @return
     */
    @Bean
    public Map<String, String> renderOptions() {
        Map<String, String> optionMap = new HashMap<>();

        optionMap.put("api.server", "http://" + apiServer);
        optionMap.put("api.server.port", apiServerPort);
        optionMap.put("file.server", apiServerPort);
        optionMap.put("api.base", apiServer + "/api/current");
        optionMap.put("api.templates", apiServer + "/api/templates");
        optionMap.put("api.profiles", apiServer + "/api/profiles");
        optionMap.put("api.lookups", apiServer + "/api/lookups");
        optionMap.put("api.files", apiServer + "/api/files");
        optionMap.put("api.nodes", apiServer + "/api/nodes");
        return optionMap;
    }
}
