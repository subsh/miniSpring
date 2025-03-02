package com.minis.minispring.aop;

import com.minis.minispring.beans.BeansException;
import com.minis.minispring.beans.factory.BeanFactory;
import com.minis.minispring.beans.factory.FactoryBean;
import com.minis.minispring.util.ClassUtils;

public class ProxyFactoryBean implements FactoryBean<Object> {
    private BeanFactory beanFactory;
    private AopProxyFactory aopProxyFactory;
    private String targetName;
    private Object target;
    private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
    private Object singletonInstance;

    private String interceptorName;

    private PointcutAdvisor advisor;

    public ProxyFactoryBean(){
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        this.aopProxyFactory = aopProxyFactory;
    }
    public AopProxyFactory getAopProxyFactory() {
        return this.aopProxyFactory;
    }

    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }

    protected AopProxy createAopProxy() {
        System.out.println("----------createAopProxy for :"+target+"--------");
        return getAopProxyFactory().createAopProxy(target, this.advisor);
    }


    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
    public Object getTarget() {
        return target;
    }
    public void setTarget(Object target) {
        this.target = target;
    }

    // 获取内部对象
    @Override
    public Object getObject() throws Exception {
        initializeAdvisor();
        return getSingletonInstance();
    }

    private synchronized void initializeAdvisor(){
        Object advice = null;
        MethodInterceptor mi = null;
        try{
            advice = this.beanFactory.getBean(this.interceptorName);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        this.advisor = (PointcutAdvisor) advice;

    }

    // 获取代理
    private synchronized Object getSingletonInstance() {
        if (this.singletonInstance == null) {
            this.singletonInstance = getProxy(createAopProxy());
        }
        System.out.println("----------return proxy for :"+this.singletonInstance+"--------"+this.singletonInstance.getClass());

        return this.singletonInstance;
    }

    // 生成代理对象
    protected Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }
    @Override
    public Class<?> getObjectType() {
        return null;
    }

}
