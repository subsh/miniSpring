package com.minis.minispring.web.context;

import com.minis.minispring.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 一个监听类，用于启动父容器
 */
public class ContextLoaderListener implements ServletContextListener {

    // 一个配置文件路径变量，IoC容器的配置文件
    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

    private WebApplicationContext context;

    public ContextLoaderListener(){}

    public ContextLoaderListener(WebApplicationContext context){
        this.context = context;
    }

    public void contextDestroyed(ServletContextEvent event){

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        initWebApplicationContext(event.getServletContext());
    }

    private void initWebApplicationContext(ServletContext servletContext) {
        // 通过配置文件参数从web.xml中得到配置文件路径，如applicationContext.xml
        String sContextLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        // 用这个配置文件创建一个新的上下文。
        WebApplicationContext wac = new XmlWebApplicationContext(sContextLocation);
        wac.setServletContext(servletContext);
        this.context = wac;
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
    }

}
