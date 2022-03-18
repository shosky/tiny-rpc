package com.leo.rpc.consumer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description: 声明代理Bean
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TinyReference {

    /**
     * 服务版本
     *
     * @return
     */
    String serviceVersion() default "1.0.0";

    /**
     * 注册中心类型
     *
     * @return
     */
    String registryType() default "ZOOKEEPER";

    /**
     * 注册中心地址
     *
     * @return
     */
    String registryAddr() default "127.0.0.1:2181";

    /**
     * 超时时间
     *
     * @return
     */
    long timeout() default 5000;
}
