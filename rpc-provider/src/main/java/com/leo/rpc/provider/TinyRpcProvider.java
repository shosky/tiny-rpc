package com.leo.rpc.provider;

import com.leo.rpc.codec.TinyRpcDecoder;
import com.leo.rpc.codec.TinyRpcEncoder;
import com.leo.rpc.common.RpcServiceHelper;
import com.leo.rpc.common.model.ServiceMeta;
import com.leo.rpc.handler.RpcIdleStateHandler;
import com.leo.rpc.handler.RpcRequestHandler;
import com.leo.rpc.provider.annotation.TinyRpcService;
import com.leo.rpc.registry.IRegistryService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description: 服务提供者
 **/
@Slf4j
public class TinyRpcProvider implements InitializingBean, BeanPostProcessor {

    private String serverAddress;
    private Integer serverPort;
    private IRegistryService registryService;
    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    public TinyRpcProvider(int serverPort, IRegistryService registryService) {
        this.serverPort = serverPort;
        this.registryService = registryService;
    }

    /**
     * InitializingBean属性注入后调用此方法。
     * 开启Provider的Netty服务端
     */
    @Override
    public void afterPropertiesSet() {
        new Thread(() -> {
            try {
                startRpcServer();
            } catch (Exception e) {
                log.error("start rpc provider error.", e);
            }
        }).start();
    }

    /**
     * 服务提供者采用的是主从 Reactor 线程模型，启动过程包括配置线程池、Channel 初始化、端口绑定三个步骤。
     *
     * @throws Exception
     */
    private void startRpcServer() throws Exception {
        serverAddress = InetAddress.getLocalHost().getHostAddress();

        EventLoopGroup boss = new NioEventLoopGroup();

        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new TinyRpcEncoder())
                                    .addLast(new TinyRpcDecoder())
                                    .addLast(new RpcIdleStateHandler())
                                    .addLast(new RpcRequestHandler(rpcServiceMap));
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.bind(serverAddress, serverPort).sync();

            log.info("server addr {} started on port {}", this.serverAddress, this.serverPort);
            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();

            worker.shutdownGracefully();
        }
    }

    /**
     * Bean生命周期扩展机制BeanPostProcessor。
     * 获取包含@TinyService注解的函数，并注册到注册中心。
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        TinyRpcService annotation = bean.getClass().getAnnotation(TinyRpcService.class);
        if (annotation != null) {
            String name = annotation.serviceInterface().getName();
            String version = annotation.serverVersion();
            try {
                ServiceMeta serviceMeta = new ServiceMeta().builder().addr(serverAddress).port(serverPort).name(name).version(version).build();
                //获取Provider服务内添加@TinyRpcService注解的方法注入注册中心
                registryService.register(serviceMeta);
                //com.leo.rpc.provider.facade.HelloFacade#1.0.0:HelloFacade对象Bean
                rpcServiceMap.put(RpcServiceHelper.buildServiceKey(serviceMeta.getName(), serviceMeta.getVersion()), bean);
            } catch (Exception e) {
                log.error("failed to register service {}#{}", name, version, e);
            }
        }
        return bean;
    }


}
