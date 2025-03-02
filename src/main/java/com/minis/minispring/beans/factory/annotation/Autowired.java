package com.minis.minispring.beans.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实现Autowired注解，好处在于不需要显示地在XML配置文件中使用ref属性
 */

@Target(ElementType.FIELD) // 用于指定自定义注解的适用范围，FIELD表示注解只能用于字段（类的成员变量）
@Retention(RetentionPolicy.RUNTIME)
// 用于指定自定义注解的生命周期，即注解在编译后是否保留，以及保留到哪个阶段。RUNTIME表示注解的生命周期是运行时，可以通过反射获取
public @interface Autowired {
}
