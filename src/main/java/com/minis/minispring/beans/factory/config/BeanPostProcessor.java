package com.minis.minispring.beans.factory.config;

import com.minis.minispring.beans.BeansException;
import com.minis.minispring.beans.factory.BeanFactory;

/**
 * Bean处理器，可以在bean初始化前、中、后分别对bean进行处理
 */

public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

    void setBeanFactory(BeanFactory beanFactory);
}
