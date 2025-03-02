package com.minis.minispring.aop.framework.autoproxy;

import com.minis.minispring.aop.*;
import com.minis.minispring.beans.BeansException;
import com.minis.minispring.beans.factory.BeanFactory;
import com.minis.minispring.beans.factory.config.BeanPostProcessor;
import com.minis.minispring.util.PatternMatchUtils;

public class BeanNameAutoProxyCreator implements BeanPostProcessor {
    // 代理对象名称模式，如action*
    String pattern;
    private BeanFactory beanFactory;
    private AopProxyFactory aopProxyFactory;
    private String interceptorName;
    private PointcutAdvisor advisor;

    public BeanNameAutoProxyCreator() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }

    public void setAdvisor(PointcutAdvisor advisor) {
        this.advisor = advisor;
    }

    // 核心方法，在bean实例化后，init-method调用之前执行这个步骤
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(isMatch(beanName, this.pattern)){
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setTarget(bean);
            proxyFactoryBean.setBeanFactory(beanFactory);
            proxyFactoryBean.setAopProxyFactory(aopProxyFactory);
            proxyFactoryBean.setInterceptorName(interceptorName);
            bean = proxyFactoryBean;
            return proxyFactoryBean;
        }else{
            return bean;
        }
    }

    protected AopProxy createAopProxy(Object target) {
        return this.aopProxyFactory.createAopProxy(target,this.advisor);
    }
    protected Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    protected boolean isMatch(String beanName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, beanName);
    }

}
