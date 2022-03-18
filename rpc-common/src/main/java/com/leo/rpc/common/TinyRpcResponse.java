package com.leo.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
@Data
public class TinyRpcResponse implements Serializable {

    private Object data; // 请求结果

    private String message; // 错误信息

}

