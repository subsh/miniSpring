package com.minis.minispring.beans.factory.support;

import com.minis.minispring.beans.factory.config.BeanDefinition;

/**
 * 存放BeanDefinition的仓库
 */

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String name, BeanDefinition beanDefinition);
    void removeBeanDefinition(String name);
    BeanDefinition getBeanDefinition(String name);
    boolean containsBeanDefinition(String name);
}
