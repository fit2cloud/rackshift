package io.rackshift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JobConfig {
    @Bean
    public Map<String, Class> job() {
        return new HashMap<>();
    }
}
