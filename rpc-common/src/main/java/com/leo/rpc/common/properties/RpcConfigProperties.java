package com.leo.rpc.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description: 提取了三个参数，分别为服务暴露的端口 servicePort、注册中心的地址 registryAddr 和注册中心的类型 registryType。
 **/
@Data
@ConfigurationProperties(prefix = "rpc.config")
public class RpcConfigProperties {

    private int servicePort;

    private String registryAddr;

    private String registryType;
}
