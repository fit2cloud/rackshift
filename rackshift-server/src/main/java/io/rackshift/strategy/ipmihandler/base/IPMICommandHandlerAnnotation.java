package io.rackshift.strategy.ipmihandler.base;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Service
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IPMICommandHandlerAnnotation {
    /**
     * 该类要处理的ipmi命令
     *
     * @return
     */
    String operation() default "status";

    /**
     * 注释
     *
     * @return
     */
    String desc();
}

