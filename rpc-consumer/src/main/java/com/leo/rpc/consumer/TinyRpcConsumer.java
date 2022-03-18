package com.leo.rpc.consumer;

import com.leo.rpc.codec.TinyRpcDecoder;
import com.leo.rpc.codec.TinyRpcEncoder;
import com.leo.rpc.common.RpcServiceHelper;
import com.leo.rpc.common.TinyRpcRequest;
import com.leo.rpc.common.model.ServiceMeta;
import com.leo.rpc.handler.RpcResponseHandler;
import com.leo.rpc.protocol.TinyRpcProtocol;
import com.leo.rpc.registry.IRegistryService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
@Slf4j
public class TinyRpcConsumer {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public TinyRpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new TinyRpcEncoder())
                                .addLast(new TinyRpcDecoder())
                                .addLast(new RpcResponseHandler());
                    }
                });
    }

    public void sendRequest(TinyRpcProtocol<TinyRpcRequest> protocol, IRegistryService registryService) throws Exception {
        TinyRpcRequest request = protocol.getBody();
        Object[] params = request.getParams();
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());

        int invokerHashCode = params.length > 0 ? params[0].hashCode() : serviceKey.hashCode();
        ServiceMeta serviceMetadata = registryService.discovery(serviceKey, invokerHashCode);

        if (serviceMetadata != null) {
            ChannelFuture future = bootstrap.connect(serviceMetadata.getAddr(), serviceMetadata.getPort()).sync();
            future.addListener((ChannelFutureListener) arg0 -> {
                if (future.isSuccess()) {
                    log.info("connect rpc server {} on port {} success.", serviceMetadata.getAddr(), serviceMetadata.getPort());
                } else {
                    log.error("connect rpc server {} on port {} failed.", serviceMetadata.getAddr(), serviceMetadata.getPort());
                    future.cause().printStackTrace();
                    eventLoopGroup.shutdownGracefully();
                }
            });
            future.channel().writeAndFlush(protocol);
        }
    }

}
