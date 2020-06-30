package io.rackshift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;

@ServletComponentScan
@PropertySource(value = {"file:/opt/rackshift/conf/rackshift.properties"}, encoding = "UTF-8", ignoreResourceNotFound = true)
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
