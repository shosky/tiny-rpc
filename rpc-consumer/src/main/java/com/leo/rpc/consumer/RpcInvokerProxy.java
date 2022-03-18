package com.leo.rpc.consumer;

import com.leo.rpc.common.TinyRpcFuture;
import com.leo.rpc.common.TinyRpcRequest;
import com.leo.rpc.common.TinyRpcRequestHolder;
import com.leo.rpc.common.TinyRpcResponse;
import com.leo.rpc.protocol.MsgHeader;
import com.leo.rpc.protocol.MsgType;
import com.leo.rpc.protocol.ProtocolConstants;
import com.leo.rpc.protocol.TinyRpcProtocol;
import com.leo.rpc.registry.IRegistryService;
import com.leo.rpc.serialization.enums.SerializationTypeEnum;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author: leo wang
 * @date: 2022-03-17
 * @description:
 **/
public class RpcInvokerProxy implements InvocationHandler {

    private final String serviceVersion;

    private final long timeout;

    private final IRegistryService registryService;

    public RpcInvokerProxy(String serviceVersion, long timeout, IRegistryService registryService) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryService = registryService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建 RPC 协议 对象
        TinyRpcProtocol<TinyRpcRequest> protocol = new TinyRpcProtocol<>();

        MsgHeader header = new MsgHeader();
        long requestId = TinyRpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setRequestId(requestId);
        header.setSerialization((byte) SerializationTypeEnum.HESSIAN.getType());
        header.setMsgType((byte) MsgType.REQUEST.getType());
        header.setStatus((byte) 0x1);
        protocol.setHeader(header);

        TinyRpcRequest request = new TinyRpcRequest();
        request.setServiceVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(args);
        protocol.setBody(request);

        TinyRpcConsumer rpcConsumer = new TinyRpcConsumer();
        TinyRpcFuture<TinyRpcResponse> future = new TinyRpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
        TinyRpcRequestHolder.REQUEST_MAP.put(requestId, future);

        //发起远程调用
        rpcConsumer.sendRequest(protocol, registryService);

        //等待RPC调用执行结果
        return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();

    }
}
