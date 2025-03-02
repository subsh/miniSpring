package com.minis.minispring.web.context;

import com.minis.minispring.context.ApplicationContext;

import javax.servlet.ServletContext;

/**
 * 一个上下文接口，应用在Web项目里，这个上下文接口指向了Servlet容器本身的上下文ServletContext
 */
public interface WebApplicationContext extends ApplicationContext {
    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

    ServletContext getServletContext();
    void setServletContext(ServletContext servletContext);
}
