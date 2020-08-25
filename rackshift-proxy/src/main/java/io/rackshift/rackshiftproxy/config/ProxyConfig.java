package io.rackshift.rackshiftproxy.config;

import org.apache.velocity.app.Velocity;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Component
@EnableAspectJAutoProxy
public class ProxyConfig {

    @PostConstruct
    public void initVelocity() {
        Properties ve = new Properties();
        ve.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(ve);
    }
}
