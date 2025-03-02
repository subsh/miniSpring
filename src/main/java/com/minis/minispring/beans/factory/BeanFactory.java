package com.minis.minispring.beans.factory;

import com.minis.minispring.beans.BeansException;

public interface BeanFactory {
    Object getBean(String BeanName) throws BeansException;
    boolean containsBean(String name);
    boolean isSingleton(String name);
    boolean isPrototype(String name);
    Class<?> getType(String name);


    // void registerBean(String beanName, Object obj);
}
