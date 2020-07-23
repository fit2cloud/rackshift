package io.rackshift.strategy.ipmihandler.base;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;

@Component
public class AnnotationInjector implements BeanPostProcessor {

    @Resource
    IPMIHandlerDecorator ipmiHandlerDecorator;

    /**
     * 注入对应的处理器
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Annotation annotation = bean.getClass().getDeclaredAnnotation(IPMICommandHandlerAnnotation.class);
        if (annotation != null) {
            ipmiHandlerDecorator.getHandlerMap().put(((IPMIHandlerInterface) bean).getClass().getAnnotation(IPMICommandHandlerAnnotation.class).operation(), (IPMIHandlerInterface) bean);
        }
        return bean;
    }
}
