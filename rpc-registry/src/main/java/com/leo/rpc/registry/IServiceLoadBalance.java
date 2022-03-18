package com.leo.rpc.registry;

import java.util.List;

/**
 * @author: leo wang
 * @date: 2022-03-17
 * @description:
 **/
public interface IServiceLoadBalance<T> {

    T select(List<T> servers, int hashCode);
}
