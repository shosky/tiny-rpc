package com.leo.rpc.common;

import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
@Data
public class TinyRpcFuture<T> {
    private Promise<T> promise;

    private long timeout;

    public TinyRpcFuture(Promise<T> promise, long timeout) {
        this.promise = promise;
        this.timeout = timeout;
    }
}
