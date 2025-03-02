package com.minis.minispring.aop;

import java.lang.reflect.Method;

public interface MethodBeforAdvice extends BeforeAdvice{
    void before(Method method, Object[] args, Object target) throws Throwable;
}
