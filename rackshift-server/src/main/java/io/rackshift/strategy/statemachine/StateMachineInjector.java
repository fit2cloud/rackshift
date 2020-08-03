package io.rackshift.strategy.statemachine;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StateMachineInjector implements BeanPostProcessor {
    @Resource
    private StateMachine stateMachine;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        EventHandlerAnnotation annotation = bean.getClass().getDeclaredAnnotation(EventHandlerAnnotation.class);
        if (annotation != null) {
            stateMachine.configureHandler(annotation.value(), (IStateHandler) bean);
        }
        return bean;
    }
}
