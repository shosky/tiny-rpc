package com.leo.rpc.handler;

import com.leo.rpc.common.model.HeartBeatData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * @author: leo wang
 * @date: 2022-03-18
 * @description: 心跳检测
 **/
public class RpcHeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        doHeartBeatTask(ctx);
    }

    /**
     * 心跳检测任务
     *
     * @param ctx
     */
    private void doHeartBeatTask(ChannelHandlerContext ctx) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                HeartBeatData heartBeatData = buildHeartBeatData();
                ctx.writeAndFlush(heartBeatData);
                doHeartBeatTask(ctx);
            }
        }, 10, TimeUnit.SECONDS);
    }

    public HeartBeatData buildHeartBeatData(){
        return new HeartBeatData();
    }
}
