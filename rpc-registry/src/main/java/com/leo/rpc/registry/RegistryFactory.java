package com.leo.rpc.registry;

import com.leo.rpc.registry.service.ZooKeeperRegistryService;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description: 服务注册工厂
 **/
public class RegistryFactory {
    public static volatile IRegistryService registryService;

    public static IRegistryService getInstance(String registerAddr, RegistryType type) throws Exception {

        if (null == registryService) {
            synchronized (RegistryFactory.class) {
                if (null == registryService) {
                    switch (type) {
                        case ZOOKEEPER:
                            registryService = new ZooKeeperRegistryService(registerAddr);
                            break;
                    }
                }
            }
        }
        return registryService;
    }

}
