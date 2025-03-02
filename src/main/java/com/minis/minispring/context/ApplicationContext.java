package com.minis.minispring.context;

import com.minis.minispring.beans.BeansException;
import com.minis.minispring.beans.factory.ListableBeanFactory;
import com.minis.minispring.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.minispring.beans.factory.config.ConfigurableBeanFactory;
import com.minis.minispring.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.minispring.core.env.Environment;
import com.minis.minispring.core.env.EnvironmentCapable;

/**
 * 将这个接口作为公共接口，所有的上下文都实现它，支持上下文环境和事件发布
 */
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, ConfigurableBeanFactory, ApplicationEventPublisher {
    String getApplicationName();

    long getStartupDate();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    void setEnvironment(Environment environment);

    Environment getEnvironment();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

    void refresh() throws BeansException, IllegalStateException;

    void close();

    boolean isActive();
}
