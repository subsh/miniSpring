package com.minis.minispring.beans.factory;

import com.minis.minispring.beans.BeansException;

import java.util.Map;

/**
 *  将Factory内部管理的Bean作为一个集合来对待，用这个接口来获取这些Bean的基本信息
 */
public interface ListableBeanFactory extends BeanFactory{
    boolean containsBeanDefinition(String beanName);
    int getBeanDefinitionCount();
    String[] getBeanDefinitionNames();
    String[] getBeanNamesForType(Class<?> type);
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;
}
