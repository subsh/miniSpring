package com.minis.minispring.beans.factory.config;

import com.minis.minispring.beans.factory.ListableBeanFactory;

/**
 * 用这个接口将多个接口合并在一起，这些接口都给通用的BeanFactory与BeanDefinition新增了众多处理方法，用来增强各镇特性
 * 在Java中，一个接口代表的是一种特性或能力，一个个抽取出来，各自独立互不干扰。如果一个具体的类，想具备某些特性或能力，就去实现该接口。
 * 这是————接口隔离原则。
 */

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

}
