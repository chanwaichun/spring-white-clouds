package com.byx.pub.filter;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 白名单注解
 * @author jump
 * @date 2020/11/27 11:58
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterPass {
    public String value() default "";
}