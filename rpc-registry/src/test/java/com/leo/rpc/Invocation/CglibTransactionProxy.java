package com.leo.rpc.Invocation;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author: leo wang
 * @date: 2022-03-17
 * @description:
 **/
public class CglibTransactionProxy implements MethodInterceptor {

    private Object target;

    public CglibTransactionProxy(Object target) {
        this.target = target;
    }

    public Object genProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("start cglib transaction");
        Object result = methodProxy.invokeSuper(o, args);
        System.out.println("submit cglib transaction");
        return result;
    }
}
