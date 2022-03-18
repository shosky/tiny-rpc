package com.leo.rpc.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author: leo wang
 * @date: 2022-03-18
 * @description: 空闲检测
 **/
public class RpcIdleStateHandler extends IdleStateHandler {

    public RpcIdleStateHandler() {
        super(60, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        ctx.channel().close();
    }
}
