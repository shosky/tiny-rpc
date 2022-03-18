package com.leo.rpc.handler;

import com.leo.rpc.common.TinyRpcFuture;
import com.leo.rpc.common.TinyRpcRequestHolder;
import com.leo.rpc.common.TinyRpcResponse;
import com.leo.rpc.protocol.TinyRpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
public class RpcResponseHandler extends SimpleChannelInboundHandler<TinyRpcProtocol<TinyRpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TinyRpcProtocol<TinyRpcResponse> msg) throws Exception {
        long requestId = msg.getHeader().getRequestId();

        TinyRpcFuture<TinyRpcResponse> future = TinyRpcRequestHolder.REQUEST_MAP.remove(requestId);

        future.getPromise().setSuccess(msg.getBody());
    }
}
