package com.minis.minispring.beans.factory.support;

import com.minis.minispring.beans.BeansException;
import com.minis.minispring.beans.factory.config.AbstractAutowireCapableBeanFactory;
import com.minis.minispring.beans.factory.config.BeanDefinition;
import com.minis.minispring.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * IoC引擎,它继承了其他BeanFactory类来实现Bean的创建管理功能
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {

    ConfigurableListableBeanFactory parentBeanFactory;
    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames(){
        return (String[]) this.beanDefinitionNames.toArray();
    }

    public String[] getBeanNamesForType(Class<?> type){
        List<String> result = new ArrayList<>();
        for(String beanName : this.beanDefinitionNames){
            boolean matchFound = false;
            BeanDefinition mbd = this.getBeanDefinition(beanName);
            Class<?> classToMatch = mbd.getClass();
            // 检查classToMath是否是type的子类或实现类
            if(type.isAssignableFrom(classToMatch)){
                matchFound = true;
            }else{
                matchFound = false;
            }

            if(matchFound){
                result.add(beanName);
            }
        }
        return (String[]) result.toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException{
        String[] beanNames = getBeanNamesForType(type);
        Map<String, T> result = new LinkedHashMap<>(beanNames.length);
        for(String beanName : beanNames){
            Object beanInstance = getBean(beanName);
            result.put(beanName, (T) beanInstance);
        }

        return result;
    }

    public void setParent(ConfigurableListableBeanFactory beanFactory){
        this.parentBeanFactory = beanFactory;
    }

    // 当调用 getBean() 获取 Bean 时，先从
    //WebApplicationContext 中获取，若为空则通过 parentApplicationContext 获取
    public Object getBean(String beanName) throws BeansException{
        Object result = super.getBean(beanName);
        if(result == null){
            result = this.parentBeanFactory.getBean(beanName);
        }

        return result;
    }

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {

    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return new String[0];
    }

    @Override
    public String[] getDependenciesForBean(String beanName) {
        return new String[0];
    }
}
