package com.leo.rpc.handler;

import com.leo.rpc.common.RpcServiceHelper;
import com.leo.rpc.common.TinyRpcRequest;
import com.leo.rpc.common.TinyRpcResponse;
import com.leo.rpc.protocol.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;

import java.util.Map;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<TinyRpcProtocol<TinyRpcRequest>> {

    private final Map<String, Object> rpcServiceMap;

    public RpcRequestHandler(Map<String, Object> map) {
        this.rpcServiceMap = map;
    }

    protected void channelRead0(ChannelHandlerContext ctx, TinyRpcProtocol<TinyRpcRequest> protocol) {

        /**
         * 独立线程池处理RPC调用
         */
        RpcRequestProcessor.submitRequest(() -> {
            TinyRpcProtocol<TinyRpcResponse> resProtocol = new TinyRpcProtocol<>();
            TinyRpcResponse response = new TinyRpcResponse();
            MsgHeader header = protocol.getHeader();
            header.setMsgType((byte) MsgType.RESPONSE.getType());

            try {
                Object result = handle(protocol.getBody());
                response.setData(result);
                header.setStatus((byte) MsgStatus.SUCCESS.getCode());
                resProtocol.setHeader(header);
                resProtocol.setBody(response);
            } catch (Throwable throwable) {
                header.setStatus((byte) MsgStatus.FAIL.getCode());
                response.setMessage(throwable.toString());
                log.error("process request {} error", header.getRequestId(), throwable);
            }
            ctx.writeAndFlush(resProtocol);
        });

    }

    /**
     * 调用RPC服务
     *
     * @param request
     * @return
     * @throws Throwable
     */
    private Object handle(TinyRpcRequest request) throws Throwable {
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        Object serviceBean = rpcServiceMap.get(serviceKey);

        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParams();

        FastClass fastClass = FastClass.create(serviceClass);
        int methodIndex = fastClass.getIndex(methodName, parameterTypes);
        return fastClass.invoke(methodIndex, serviceBean, parameters);
    }
}
