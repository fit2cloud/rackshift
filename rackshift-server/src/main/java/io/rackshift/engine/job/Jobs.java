package io.rackshift.engine.job;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DependsOn("springUtils")
public @interface Jobs {
    //注入到容器的对应 rackhd 原始 job 的名称
    String value();
}
