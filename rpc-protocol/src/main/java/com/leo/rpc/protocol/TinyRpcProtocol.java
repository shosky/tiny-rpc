package com.leo.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description: Tiny-RPC 消息协议
 **/
@Data
public class TinyRpcProtocol<T> implements Serializable {

    private MsgHeader header;

    //消息体：TinyRpcRequest、TinyRpcResponse
    private T body;
}
