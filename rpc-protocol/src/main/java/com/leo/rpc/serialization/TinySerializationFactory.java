package com.leo.rpc.serialization;

import com.leo.rpc.serialization.enums.SerializationTypeEnum;
import com.leo.rpc.serialization.impl.HessianSerialization;
import com.leo.rpc.serialization.impl.JsonSerialization;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
public class TinySerializationFactory {

    public static TinyRpcSerialization getSerialization(byte serializationType) {
        SerializationTypeEnum byType = SerializationTypeEnum.findByType(serializationType);
        switch (byType) {
            case HESSIAN:
                return new HessianSerialization();
            case JSON:
                return new JsonSerialization();
            default:
                throw new IllegalArgumentException("serialization type is illegal," + serializationType);
        }
    }
}
