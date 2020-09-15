package io.rackshift.rackshiftproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"file:/opt/rackshift/conf/rackshift.properties"}, encoding = "UTF-8", ignoreResourceNotFound = true)
public class RackshiftProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(RackshiftProxyApplication.class, args);
    }

}
