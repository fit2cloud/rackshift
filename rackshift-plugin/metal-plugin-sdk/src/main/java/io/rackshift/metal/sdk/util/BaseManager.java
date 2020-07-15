package io.rackshift.metal.sdk.util;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class BaseManager {
    private Class<? extends Annotation> pluginAnnotationType;
    private String pluginBasePackage;
    private ListableBeanFactory context;

    public BaseManager(Class<? extends Annotation> pluginAnnotationType, String pluginBasePackage) {
        this.pluginAnnotationType = pluginAnnotationType;
        this.pluginBasePackage = pluginBasePackage;
    }

    public void init() {
        loadContext();
    }

    private void loadContext() {
        if (context == null) {
            // Create a parent context containing all beans provided to plugins
            // More on that below in the article...
            GenericApplicationContext parentContext = new GenericApplicationContext();
            parentContext.refresh();

            // Create the annotation-based context
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            context.setParent(parentContext);

            // Scan for classes annotated with @<PluginAnnotaionType>,
            // do not include standard Spring annotations in scan
            ClassPathBeanDefinitionScanner scanner =
                    new ClassPathBeanDefinitionScanner(context, false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(pluginAnnotationType));
            scanner.scan(pluginBasePackage);
            context.refresh();
            this.context = context;
        }

    }

    private Collection getPluginDescriptors(ListableBeanFactory context) {
        return context.getBeansWithAnnotation(pluginAnnotationType).values();
    }


    public String getPluginBasePackage() {
        return pluginBasePackage;
    }


    protected Collection getPlugins() {
        loadContext();
        return getPluginDescriptors(context);
    }

}
