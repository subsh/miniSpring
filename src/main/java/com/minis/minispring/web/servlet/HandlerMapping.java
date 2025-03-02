package com.minis.minispring.web.servlet;

import com.minis.minispring.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    HandlerMethod getHandler(HttpServletRequest request) throws Exception;
}
