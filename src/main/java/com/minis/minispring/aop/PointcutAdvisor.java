package com.minis.minispring.aop;

/**
 * PointcutAdvisor 接口扩展了 Advisor，内部可以返回 Pointcut，也就是说这个 Advisor 有
 * 一个特性：能支持切点 Pointcut 了。
 */
public interface PointcutAdvisor extends Advisor{
    Pointcut getPointcut();
}
