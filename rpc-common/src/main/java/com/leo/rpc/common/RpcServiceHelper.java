package com.leo.rpc.common;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
public class RpcServiceHelper {

    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("#", serviceName, serviceVersion);
    }
}
