package io.rackshift.state.handler;

import io.rackshift.state.LifeEvent;
import io.rackshift.state.LifeStatus;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StateHandlerAnnotation {

    /**
     * 当前状态
     *
     * @return
     */
    LifeStatus curStatus();

    /**
     * 事件
     *
     * @return
     */
    LifeEvent event();


    /**
     * 下一步状态
     *
     * @return
     */
    LifeStatus nextStatus();
}
