package io.rackshift.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Bean获取工具
 * <p>使用方法：
 * <br>
 * spring配置文件中添加
 * &lt;context:component-scan base-package="com.zwzx.common.spring"/&gt;
 * <p>
 *
 * @author kun.mo
 */
@Component
public class CommonBeanFactory implements ApplicationContextAware {

    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        context = ctx;
    }

    /**
     * 根据名称获取Bean
     *
     * @param beanName bean名称
     * @return Bean实例
     */
    public static Object getBean(String beanName) {
        if (context == null || StringUtils.isBlank(beanName)) {
            return null;
        }
        return context.getBean(beanName);
    }

    /**
     * 根据类型获取Bean
     *
     * @param className bean类型
     * @param <T>       bean类型
     * @return Bean实例
     */
    public static <T> T getBean(Class<T> className) {
        if (context == null || className == null) {
            return null;
        }
        return context.getBean(className);
    }
}
