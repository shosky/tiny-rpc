package com.leo.rpc.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
public class TinyRpcRequestHolder {

    public final static AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    /**
     * 异步回调Map
     */
    public static final Map<Long, TinyRpcFuture<TinyRpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();

}
