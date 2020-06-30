package io.rackshift.config;

import io.rackshift.utils.Translator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class InternationalConfig {

    @Value("${spring.messages.basename}")
    private String baseName;

    @Bean("messageSource")
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(baseName);
        return messageSource;
    }

    @Bean
    public Translator getTranslator() {
        return new Translator();
    }
}
