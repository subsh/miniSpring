package com.minis.minispring.aop;

/**
 * 通知者，是一个管理类，它包含了一个advice，还能寻找符合条件的方法名进行增强
 */
public interface Advisor {
    MethodInterceptor getMethodInterceptor();

    void setMethodInterceptor(MethodInterceptor methodInterceptor);

    Advice getAdvice();
}
