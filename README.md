# tiny-rpc

![http://r8x6hblk3.hn-bkt.clouddn.com/tiny-rpc-architecture.png](http://r8x6hblk3.hn-bkt.clouddn.com/tiny-rpc-architecture.png)

# tiny-rpc介绍

## 交互流程
- 服务端在启动后，会将它提供的服务列表发布到注册中心，客户端向注册中心订阅服务地址。
- 客户端通过本地代理模块Proxy调用服务端，Proxy模块收到负责将方法、参数等数据转化成网络字节流。
- 客户端从服务列表中选取其中一个服务地址，并将数据通过网络发送服务端。
- 服务端收到数据后进行解码，得到请求信息。
- 服务端根据解码后的请求信息调用对应的服务，然后将调用返回给客户端。

## 知识点
1. 线程模型：Netty的主从线程模型(Reactor)
2. 通信协议设计：自定义消息协议
3. 同步/异步调用：基于Netty.Promise异步调用
4. 负载均衡：基于一致性哈希算法
5. 动态代理：基于Cglib实现Service注入，基于代理机制增强Service，实现无感调用
6. 服务注册与发现：采用Zookeeper实现服务注册与发现

## zk节点目录
- tiny_rpc
  - com.tiny.kv.rpc.facade.VoteFacade#1.0.0
    - 127.0.0.1:2781
    - 127.0.0.1:2782
    - 127.0.0.1:2783
  - com.tiny.kv.rpc.facade.HeartFacade#1.0.0

## 使用方式
1. 启动Zookeeper。 
`docker pull zookeeper & docker run --name my_zookeeper -d zookeeper:latest` 
2. 启动Consumer服务：
`mvn clean springboot:run` 
3. 启动Provider服务：
`mvn clean springboo:run`
4. `curl http://127.0.0.1:8080/hello`

输出结果
```shell
curl http://127.0.0.1:8080/hello

hello tiny rpc
```
