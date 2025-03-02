package com.minis.minispring.beans.factory.xml;

import com.minis.minispring.beans.*;
import com.minis.minispring.beans.factory.config.AutowireCapableBeanFactory;
import com.minis.minispring.beans.factory.config.ConstructorArgumentValue;
import com.minis.minispring.beans.factory.config.ConstructorArgumentValues;
import com.minis.minispring.beans.factory.config.BeanDefinition;
import com.minis.minispring.beans.factory.support.AbstractBeanFactory;
import com.minis.minispring.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析bean.xml资源，加载bean定义到BeanFactory中
 */

public class XmlBeanDefinitionReader {
    AbstractBeanFactory beanFactory;
    public XmlBeanDefinitionReader(AbstractBeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }

    public void loadBeanDefinitions(Resource resource){
        while(resource.hasNext()){
            Element element = (Element) resource.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);

            // 处理构造器参数
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues AVS = new ConstructorArgumentValues();
            for(Element e : constructorElements){
                String aType = e.attributeValue("type");
                String aName = e.attributeValue("name");
                String aValue = e.attributeValue("value");
                AVS.addArgumentValue(new ConstructorArgumentValue(aValue, aType, aName));
            }
            beanDefinition.setConstructorArgumentValues(AVS);

            // 处理属性
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for(Element e : propertyElements){
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                if(pValue != null && !pValue.equals("")){
                    isRef = false;
                    pV = pValue;
                }else if(pRef != null && !pRef.equals("")){
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);
                }

                PVS.addPropertyValue(new PropertyValue(pV, pType, pName, isRef));
            }
            beanDefinition.setPropertyValues(PVS);

            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            this.beanFactory.registerBeanDefinition(beanId, beanDefinition);
        }
    }
}
