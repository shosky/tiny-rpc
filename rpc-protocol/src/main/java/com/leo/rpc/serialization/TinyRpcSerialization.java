package com.leo.rpc.serialization;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description: 序列化接口
 **/
public interface TinyRpcSerialization {

    /**
     * encode编码
     * @param obj
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> byte[] serialize(T obj) throws Exception;

    /**
     * decode解码
     * @param data
     * @param clz
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T deserialize(byte[] data, Class<T> clz) throws Exception;
}
