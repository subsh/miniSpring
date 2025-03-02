package com.minis.minispring.web.context.support;

import com.minis.minispring.context.ClassPathXmlApplicationContext;
import com.minis.minispring.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

public class XmlWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;

    public XmlWebApplicationContext(String fileName){
        super(fileName);
    }

    public ServletContext getServletContext(){
        return this.servletContext;
    }

    public void setServletContext(ServletContext servletContext){
        this.servletContext = servletContext;
    }
}
