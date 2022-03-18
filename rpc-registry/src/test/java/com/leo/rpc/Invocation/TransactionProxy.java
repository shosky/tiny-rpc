package com.leo.rpc.Invocation;

import java.lang.reflect.Proxy;

/**
 * @author: leo wang
 * @date: 2022-03-17
 * @description:
 **/
public class TransactionProxy {

    private Object target;

    public TransactionProxy(Object target) {
        this.target = target;
    }

    public Object getProxyInstance() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    System.out.println("start transaction");
                    Object result = method.invoke(target, args);
                    System.out.println("submit transaction");
                    return result;
                });
    }
}
