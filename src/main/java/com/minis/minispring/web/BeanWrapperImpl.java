package com.minis.minispring.web;

import com.minis.minispring.beans.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanWrapperImpl extends AbstractPropertyAccessor {
    // 目标对象
    Object wrappedObject;
    Class<?> clz;

    public BeanWrapperImpl(Object object) {
        super();
        this.wrappedObject = object;
        this.clz = object.getClass();
    }

    public void setBeanInstance(Object object){
        this.wrappedObject = object;
    }

    public Object getBeanInstance(){
        return wrappedObject;
    }

    // 绑定具体某个参数值
    public void setPropertyValue(PropertyValue pv) throws NoSuchFieldException {
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());
        PropertyEditor pe = this.getCustomEditor(propertyHandler.getPropertyClz());
        if (pe == null) {
            pe = this.getDefaultEditor(propertyHandler.getPropertyClz());

        }
        if (pe != null) {
            pe.setAsText((String) pv.getValue());
            propertyHandler.setValue(pe.getValue());
        }
        else {
            propertyHandler.setValue(pv.getValue());
        }

    }

    // 一个内部类，用于处理参数，通过getter()和setter()操作属性
    class BeanPropertyHandler{
        Method writeMethod = null;
        Method readMethod = null;
        Class<?> propertyClz = null;

        public Class<?> getPropertyClz(){
            return propertyClz;
        }

        public BeanPropertyHandler(String propertyName) throws NoSuchFieldException {
            try{
                // 获取参数对应的属性及类型
                Field field = clz.getDeclaredField(propertyName);
                propertyClz = field.getType();

                // 获取设置属性的方法，按照约定为setXxx()
                this.writeMethod = clz.getDeclaredMethod("set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), propertyClz);
                // 获取读属性的方法，按照约定为getXxx()
                this.readMethod = clz.getDeclaredMethod("get" + propertyName.substring(0,1).toUpperCase() + propertyName.substring(1));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        // 调用getter读属性值
        public Object getValue(){
            Object result = null;
            readMethod.setAccessible(true);
            try{
                result = readMethod.invoke(wrappedObject);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return result;
        }

        // 调用setter设置属性
        public void setValue(Object value){
            writeMethod.setAccessible(true);
            try{
                writeMethod.invoke(wrappedObject, value);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}





















