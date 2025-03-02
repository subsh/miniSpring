package com.minis.minispring.beans.factory.annotation;

import com.minis.minispring.beans.BeansException;
import com.minis.minispring.beans.factory.BeanFactory;
import com.minis.minispring.beans.factory.config.AutowireCapableBeanFactory;
import com.minis.minispring.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 *  利用反射获取所有标注了Autowired注解的成员变量，把它初始化成一个Bean，然后注入属性。
 */

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;

        Class<?> clz = bean.getClass();
        Field[] fields = clz.getDeclaredFields();
        if(fields != null){
            // 对每一个属性进行判断，如果有带@Autowired注解则进行处理
            for(Field field : fields){
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                if(isAutowired){
                    // 根据属性名查找同名的bean
                    String fieldName = field.getName();
                    Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                    // 设置属性值，完成注入
                    try{
                        field.setAccessible(true);
                        field.set(bean, autowiredObj);
                        System.out.println("autowire " + fieldName + " for bean " + beanName);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public BeanFactory getBeanFactory(){
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }
}
