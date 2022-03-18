package com.leo.rpc.registry;

import com.leo.rpc.common.model.ServiceMeta;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description: 注册中心接口
 **/
public interface IRegistryService {

    /**
     * 服务注册
     *
     * @param serviceMeta
     * @throws Exception
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务卸载
     *
     * @param serviceMeta
     * @throws Exception
     */
    void unReigster(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务发现
     * @param serviceName
     * @param invokerHashCode
     * @return
     * @throws Exception
     */
    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

    /**
     * 销毁
     * @throws Exception
     */
    void destroy() throws Exception;
}
