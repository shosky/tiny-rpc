package com.leo.rpc.provider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TinyService {

    /**
     * 服务类型
     * @return
     */
    Class<?> serviceInterface() default Object.class;

    /**
     * 服务版本
     * @return
     */
    String serverVersion() default "1.0.0";
}
