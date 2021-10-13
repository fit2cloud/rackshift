package io.rackshift.config;

import io.rackshift.engine.job.Jobs;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Component
public class EngineConfig implements BeanPostProcessor {
    @Bean
    public Map<String, Class> job() {
        return new HashMap<>();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Annotation annotation = bean.getClass().getAnnotation(Jobs.class);
        if (annotation != null) {
            job().put(((Jobs) annotation).value(), bean.getClass());
        }
        return bean;
    }
}
