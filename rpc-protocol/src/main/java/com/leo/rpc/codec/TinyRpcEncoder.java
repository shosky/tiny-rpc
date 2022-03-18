package com.leo.rpc.codec;

import com.leo.rpc.protocol.MsgHeader;
import com.leo.rpc.protocol.TinyRpcProtocol;
import com.leo.rpc.serialization.TinyRpcSerialization;
import com.leo.rpc.serialization.TinySerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
public class TinyRpcEncoder extends MessageToByteEncoder<TinyRpcProtocol<Object>> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TinyRpcProtocol<Object> msg, ByteBuf byteBuf) throws Exception {
        MsgHeader header = msg.getHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getSerialization());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getRequestId());
        TinyRpcSerialization serialization = TinySerializationFactory.getSerialization(header.getSerialization());
        byte[] data = serialization.serialize(msg.getBody());
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);

    }
}
