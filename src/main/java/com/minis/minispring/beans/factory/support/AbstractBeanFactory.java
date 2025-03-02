package com.minis.minispring.beans.factory.support;

import com.minis.minispring.beans.BeansException;
import com.minis.minispring.beans.PropertyValue;
import com.minis.minispring.beans.PropertyValues;
import com.minis.minispring.beans.factory.BeanFactory;
import com.minis.minispring.beans.factory.FactoryBean;
import com.minis.minispring.beans.factory.config.BeanDefinition;
import com.minis.minispring.beans.factory.config.ConfigurableBeanFactory;
import com.minis.minispring.beans.factory.config.ConstructorArgumentValue;
import com.minis.minispring.beans.factory.config.ConstructorArgumentValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  基于代码复用，解耦的原则，对通用代码进行抽象，抽象出一个AbstractBeanFactory
 *  并对部分方法提供默认实现，确保这些方法即使不再被其他BeanFactory实现也能正常生效
 */

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory, BeanDefinitionRegistry {
    protected Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    protected List<String> beanDefinitionNames = new ArrayList<>();
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    public AbstractBeanFactory() {
    }

    // 对外提供一个包装方法，创建容器中所有的Bean实例
    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }

        }
    }

    // getBean(),容器核心方法
    public Object getBean(String beanName) throws BeansException {
        // 先尝试直接从容器中拿Bean实例
        Object singleton = this.getSingleton(beanName);
        // 如果此时还没有这个Bean的实例，则获取它的定义来创建实例
        if (singleton == null) {
            // 如果没有实例，则尝试从毛坯实例中获取
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                System.out.println("get bean null ---------- " + beanName);
                // 如果连毛坯都没有，则创建bean实例并注册
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                if(beanDefinition != null){
                    singleton = createBean(beanDefinition);
                    this.registerBean(beanName, singleton);

//                    if(singleton instanceof BeanFactoryAware){
//                        ((BeanFactoryAware) singleton).setBeanFactory(this);
//                    }

                    // 进行beanpostprocess位置
                    // 1.初始化前
                    singleton = applyBeanPostProcessorsBeforeInitialization(singleton, beanName);

                    // 2.初始化中
                    if(beanDefinition.getInitMethodName() != null && !beanDefinition.equals("")){
                        invokeInitMethod(beanDefinition, singleton);
                    }

                    // 3.初始化后
                    applyBeanPostProcessorsAfterInitialization(singleton, beanName);
                    this.removeSingleton(beanName);
                    this.registerBean(beanName, singleton);

                }else{
                    return null;
                }
            }
        }else{

        }
        // 处理factorybean
        if(singleton instanceof FactoryBean){
            System.out.println("factory bean -------------- " + beanName + "----------------"+singleton);
            return this.getObjectFromBeanInstance(singleton, beanName);
        }else{
            System.out.println("normal bean -------------- " + beanName + "----------------"+singleton);
        }
        return singleton;
    }



    private void invokeInitMethod(BeanDefinition beanDefinition, Object obj){
        Class<?> clz = beanDefinition.getClass();
        Method method = null;
        try{
            method = clz.getMethod(beanDefinition.getInitMethodName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try{
            method.invoke(obj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean containsBean(String name) {
        return containsSingleton(name);
    }

    public void registerBean(String beanName, Object obj){
        this.registerSingleton(beanName, obj);
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
        if (!beanDefinition.isLazyInit()) {
            try {
                getBean(name);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }

    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        // 创建毛坯bean实例
        Object obj = doCreateBean(beanDefinition);
        // 存放到毛坯实例缓存中
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);

        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 完善Bean，主要是处理属性
        populateBean(beanDefinition, clz, obj);

        return obj;
    }

    // 创建毛坯实例，仅仅调用构造方法，没有进行属性处理
    private Object doCreateBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;

        try {
            clz = Class.forName(beanDefinition.getClassName());
            // 处理构造器参数
            ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            // 如果有参数
            if (!constructorArgumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
                // 对每一个参数，分数据类型分别处理
                for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue constructorArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(constructorArgumentValue.getType()) || "java.lang.String".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    } else if ("Integer".equals(constructorArgumentValue.getType()) || "java.lang.Integer".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
                    } else if ("int".equals(constructorArgumentValue.getType()) || "java.lang.int".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
                    } else {
                        // 默认为String
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    }
                }
                try {
                    // 按照特定构造器创建实例
                    con = clz.getConstructor(paramTypes);
                    obj = con.newInstance(paramValues);
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                obj = clz.newInstance();
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(beanDefinition.getId() + " bean created. " + beanDefinition.getClassName() + " : " + obj.toString());
        return obj;
    }

    // 处理属性的方法
    private void populateBean(BeanDefinition beanDefinition, Class<?> clz, Object obj) {
        // 处理属性
        System.out.println("handle properties for bean : " + beanDefinition.getId());
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        // 如果有属性
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {

                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pType = propertyValue.getType();
                String pName = propertyValue.getName();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();
                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                if (!isRef) {
                    // 如果不是ref，只是普通属性
                    // 对每一个属性，分数据类型分别处理
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(pType) || "java.lang.int".equals(pType)) {
                        paramTypes[0] = int.class;
                    } else {
                        // 默认是String
                        paramTypes[0] = String.class;
                    }

                    paramValues[0] = pValue;
                } else {
                    // 如果是ref，则创建依赖的bean
                    try {
                        paramTypes[0] = Class.forName(pType);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        // 再次调用getBean创建ref的bean实例
                        paramValues[0] = getBean((String) pValue);
                    } catch (BeansException e) {
                        e.printStackTrace();
                    }
                }

                // 按照setXxx规范查找set()方法，调用set()方法设置属性
                String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);
                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    method.invoke(obj, paramValues);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected Object getObjectFromBeanInstance(Object beanInstance, String beanName){
        if(!(beanInstance instanceof FactoryBean)){
            return beanInstance;
        }
        Object object = null;
        FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
        object = getObjectFromFactoryBean(factory, beanName);
        return object;
    }

    abstract public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    abstract public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}

