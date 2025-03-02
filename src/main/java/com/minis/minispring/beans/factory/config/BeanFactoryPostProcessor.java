package com.minis.minispring.beans.factory.config;

import com.minis.minispring.beans.BeansException;
import com.minis.minispring.beans.factory.BeanFactory;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
