package com.leo.rpc.codec;

import com.leo.rpc.common.TinyRpcRequest;
import com.leo.rpc.common.TinyRpcResponse;
import com.leo.rpc.protocol.*;
import com.leo.rpc.serialization.TinyRpcSerialization;
import com.leo.rpc.serialization.TinySerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
public class TinyRpcDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < ProtocolConstants.HEADER_TOTAL_LEN) {
            //小于固定头部大小
            return;
        }

        in.markReaderIndex();

        short magic = in.readShort();

        if (magic != ProtocolConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal, " + magic);
        }

        byte version = in.readByte();

        byte serializeType = in.readByte();

        byte msgType = in.readByte();

        byte status = in.readByte();

        long requestId = in.readLong();

        int dataLength = in.readInt();

        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        MsgType msgTypeEnum = MsgType.findByType(msgType);

        if (msgTypeEnum == null) {
            return;
        }

        MsgHeader header = new MsgHeader();

        header.setMagic(magic);
        header.setVersion(version);
        header.setSerialization(serializeType);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setMsgType(msgType);
        header.setMsgLen(dataLength);

        TinyRpcSerialization serialization = TinySerializationFactory.getSerialization(serializeType);

        switch (msgTypeEnum) {
            case REQUEST:
                TinyRpcRequest request = serialization.deserialize(data, TinyRpcRequest.class);
                if (request != null) {
                    TinyRpcProtocol<TinyRpcRequest> protocol = new TinyRpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    out.add(protocol);
                }
                break;
            case RESPONSE:
                TinyRpcResponse response = serialization.deserialize(data, TinyRpcResponse.class);
                if (response != null) {
                    TinyRpcProtocol<TinyRpcResponse> protocol = new TinyRpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    out.add(protocol);
                }
                break;
            case HEARTBEAT:
                break;
        }
    }
}
