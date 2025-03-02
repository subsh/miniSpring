package com.minis.minispring.web.method.annotation;

import com.minis.minispring.beans.BeansException;
import com.minis.minispring.context.ApplicationContext;
import com.minis.minispring.context.ApplicationContextAware;
import com.minis.minispring.web.bind.annotation.RequestMapping;
import com.minis.minispring.web.context.WebApplicationContext;
import com.minis.minispring.web.method.HandlerMethod;
import com.minis.minispring.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class RequestMappingHandlerMapping implements HandlerMapping, ApplicationContextAware {

    ApplicationContext applicationContext;

    private MappingRegistry mappingRegistry = null;

    public RequestMappingHandlerMapping(WebApplicationContext wac){
    }

    // 建立URL与调用方法和实例的映射关系，存储在mappingRegistry中
    protected void initMappings(){
        Class<?> clz = null;
        Object obj = null;
        String[] controllerNames = this.applicationContext.getBeanDefinitionNames();

        // 扫描wac中存放的所有bean
        for(String controllerName : controllerNames){
            try{
                clz = Class.forName(controllerName);
                obj = this.applicationContext.getBean(controllerName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (BeansException e) {
                e.printStackTrace();
            }

            Method[] methods = clz.getDeclaredMethods();
            if(methods != null){
                // 检查每一个方法的声明
                for(Method method : methods){
                    boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                    // 如果该方法带有@RequestMapping注解，则建立映射关系
                    if(isRequestMapping){
                        String methodName = method.getName();
                        String urlMapping = method.getAnnotation(RequestMapping.class).value();

                        this.mappingRegistry.getUrlMappingNames().add(urlMapping);
                        this.mappingRegistry.getMappingObjs().put(urlMapping, obj);
                        this.mappingRegistry.getMappingMethods().put(urlMapping, method);
                        this.mappingRegistry.getMappingMethodNames().put(urlMapping, methodName);
                        this.mappingRegistry.getMappingClasses().put(urlMapping, clz);
                    }
                }
            }
        }
    }

    // 根据访问url查找对应的调用方法
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception{
        if (this.mappingRegistry == null) { //to do initialization
            this.mappingRegistry = new MappingRegistry();
            initMappings();
        }
        String sPath = request.getServletPath();
        if(!this.mappingRegistry.getUrlMappingNames().contains(sPath)){
            return null;
        }

        Method method = this.mappingRegistry.getMappingMethods().get(sPath);
        Object obj = this.mappingRegistry.getMappingObjs().get(sPath);
        Class<?> clz = this.mappingRegistry.getMappingClasses().get(sPath);
        String methodName = this.mappingRegistry.getMappingMethodNames().get(sPath);

        HandlerMethod handlerMethod = new HandlerMethod(method, obj, clz, methodName);

        return handlerMethod;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
