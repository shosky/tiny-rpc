package com.leo.rpc.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description: 服务元数据
 **/
@Data
@SuperBuilder
@NoArgsConstructor
public class ServiceMeta {

    private String name;

    private String version;

    private String addr;

    private int port;
}
