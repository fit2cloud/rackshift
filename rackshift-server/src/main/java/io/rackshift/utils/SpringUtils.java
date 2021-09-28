package io.rackshift.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

public class SpringUtils {

    public static ApplicationContext getApplicationContext() {
        return ContextLoader.getCurrentWebApplicationContext();
    }
}
