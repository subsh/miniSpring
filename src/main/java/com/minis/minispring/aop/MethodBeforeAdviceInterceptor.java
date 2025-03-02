package com.minis.minispring.aop;

public class MethodBeforeAdviceInterceptor implements MethodInterceptor, BeforeAdvice{
    private final MethodBeforAdvice advice;

    public MethodBeforeAdviceInterceptor(MethodBeforAdvice advice){
        this.advice = advice;
    }

    public Object invoke(MethodInvocation mi) throws Throwable{
        this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}
