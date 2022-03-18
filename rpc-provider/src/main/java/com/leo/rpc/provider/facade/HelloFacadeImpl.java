package com.leo.rpc.provider.facade;

import com.leo.rpc.provider.annotation.TinyRpcService;

/**
 * @author: leo wang
 * @date: 2022-03-17
 * @description:
 **/
@TinyRpcService(serviceInterface = HelloFacade.class)
public class HelloFacadeImpl implements HelloFacade {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
