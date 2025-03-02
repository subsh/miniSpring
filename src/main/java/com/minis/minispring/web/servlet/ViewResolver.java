package com.minis.minispring.web.servlet;

public interface ViewResolver {
    View resolveViewName(String viewName) throws Exception;
}
