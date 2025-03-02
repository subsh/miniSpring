package com.minis.minispring.aop;

/**
 * 返回一条匹配规则
 */
public interface Pointcut {
    MethodMatcher getMethodMatcher();
}
