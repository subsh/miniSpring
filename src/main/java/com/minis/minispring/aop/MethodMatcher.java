package com.minis.minispring.aop;

import java.lang.reflect.Method;

/**
 * 方法的匹配算法，看某个名是不是符不符合某个模式
 */

public interface MethodMatcher {
    boolean matches(Method method, Class<?> targetClass);
}
