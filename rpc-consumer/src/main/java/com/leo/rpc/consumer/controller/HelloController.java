package com.leo.rpc.consumer.controller;

import com.leo.rpc.consumer.annotation.TinyRpcReference;
import com.leo.rpc.provider.facade.HelloFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: leo wang
 * @date: 2022-03-17
 * @description:
 **/
@RestController
public class HelloController {

    /**
     * 1. 利用BeanFactoryPostProcessor给@TinyRpcReference修饰的依赖生成代理类(RpcReferenceBean)
     * 2. 代理类RpcReferenceBean的invoke方法中使用NettyClient发起远程调用，并用Future获得返回结果
     */
    @SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "SpringJavaInjectionPointsAutowiringInspection"})
    @TinyRpcReference
    private HelloFacade helloFacade;

    @GetMapping(value = "/hello")
    public String sayHello() {
        return helloFacade.hello("tiny rpc");
    }
}
