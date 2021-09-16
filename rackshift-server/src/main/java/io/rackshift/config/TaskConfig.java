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
        optionMap.put("file.server", optionMap.get("api.server"));
        optionMap.put("api.base", optionMap.get("api.server") + "/api/current");
        optionMap.put("api.templates", optionMap.get("api.server") + "/api/templates");
        optionMap.put("api.profiles", optionMap.get("api.server") + "/api/profiles");
        optionMap.put("api.lookups", optionMap.get("api.server") + "/api/lookups");
        optionMap.put("api.files", optionMap.get("api.server") + "/api/files");
        optionMap.put("api.nodes", optionMap.get("api.server") + "/api/nodes");
        return optionMap;
    }
}
