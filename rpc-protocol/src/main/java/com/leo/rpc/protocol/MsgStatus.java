package com.leo.rpc.protocol;

import lombok.Getter;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
public enum MsgStatus {

    SUCCESS(0),
    FAIL(1);
    @Getter
    private final int code;

    MsgStatus(int code) {
        this.code = code;
    }
}
