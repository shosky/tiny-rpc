package com.leo.rpc.provider.autoconfiguration;

import com.leo.rpc.common.properties.RpcConfigProperties;
import com.leo.rpc.provider.TinyRpcProvider;
import com.leo.rpc.registry.IRegistryService;
import com.leo.rpc.registry.RegistryFactory;
import com.leo.rpc.registry.RegistryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
@Configuration
@EnableConfigurationProperties(RpcConfigProperties.class)
public class ProviderAutoConfiguration {

    @Autowired
    private RpcConfigProperties configProperties;

    /**
     * 注入TinyRpcProvider到Spring
     *
     * @return
     */
    @Bean
    public TinyRpcProvider init() throws Exception {
        //获取配置文件中注册中心类型
        RegistryType type = RegistryType.valueOf(configProperties.getRegistryType());

        //获取注册服务Service，默认是ZookeeperService
        IRegistryService registryService = RegistryFactory.getInstance(configProperties.getRegistryAddr(), type);

        //返回TinyRpcProvider对象
        return new TinyRpcProvider(configProperties.getServicePort(), registryService);
    }
}
