# springcloud-learning

## 简介

Spring Cloud 教程，基于 Spring Cloud Greenwich 及 SpringBoot 2.1.7。

## 目录

- [Spring Cloud 学习教程](https://juejin.cn/column/6962024277382004773)

### 概述

- [Spring Cloud 整体架构概览](https://juejin.cn/post/6844903938748219406)

### Spring Cloud 组件

- [Spring Cloud Eureka：服务注册与发现](https://juejin.cn/post/6844903940312530957)

``` lua
springcloud-learning
├── eureka-server -- eureka注册中心
├── eureka-security-server -- 带登录认证的eureka注册中心
└── eureka-client -- eureka客户端
```

- [Spring Cloud Ribbon：负载均衡的服务调用](https://juejin.cn/post/6844903943084965902)

``` lua
springcloud-learning
├── eureka-server -- eureka注册中心
├── user-service -- 提供User对象CRUD接口的服务
└── ribbon-service -- ribbon服务调用测试服务
```

- [Spring Cloud Hystrix：服务容错保护](https://juejin.cn/post/6844903945026928654)

``` lua
springcloud-learning
├── eureka-server -- eureka注册中心
├── user-service -- 提供User对象CRUD接口的服务
└── hystrix-service -- hystrix服务调用测试服务
```

- [Hystrix Dashboard：断路器执行监控](https://juejin.cn/post/6844903951179972622)

``` lua
springcloud-learning
├── eureka-server -- eureka注册中心
├── user-service -- 提供User对象CRUD接口的服务
├── hystrix-service -- hystrix服务调用测试服务
├── turbine-service -- 聚合收集hystrix实例监控信息的服务
└── hystrix-dashboard -- 展示hystrix实例监控信息的仪表盘
```

- [Spring Cloud OpenFeign：基于Ribbon和Hystrix的声明式服务调用](https://juejin.cn/post/6844903959086235655)

``` lua
springcloud-learning
├── eureka-server -- eureka注册中心
├── user-service -- 提供User对象CRUD接口的服务
└── feign-service -- feign服务调用测试服务
```

- [Spring Cloud Zuul：API网关服务](https://juejin.cn/post/6844903960696848397)

``` lua
springcloud-learning
├── eureka-server -- eureka注册中心
├── user-service -- 提供User对象CRUD接口的服务
├── feign-service -- feign服务调用测试服务
├── zuul-proxy -- zuul作为网关的测试服务
└── zuul-proxy-async -- zuul作为网关的测试服务，使用Servlet3.0
```

- [Spring Cloud Config：外部集中化配置管理](https://juejin.cn/post/6844903966405296142)

``` lua
springcloud-learning
├── eureka-server -- eureka注册中心
├── config-server -- 配置中心服务
├── config-security-server -- 带安全认证的配置中心服务
└── config-client -- 获取配置的客户端服务
```

- [Spring Cloud Bus：消息总线](https://juejin.cn/post/6844903968158547976)

``` lua
springcloud-learning
├── eureka-server -- eureka注册中心
├── config-server -- 配置中心服务
└── config-client -- 获取配置的客户端服务
```

- [Spring Cloud Sleuth：分布式请求链路跟踪](https://juejin.cn/post/6844903975016366088)

``` lua
springcloud-learning
├── eureka-server -- eureka注册中心
├── user-service -- 提供User对象CRUD接口的服务
└── ribbon-service -- ribbon服务调用测试服务
```

- [Spring Cloud Consul：服务治理与配置中心](https://juejin.cn/post/6844903976710701063)

``` lua
springcloud-learning
├── consul-config-client -- 用于演示consul作为配置中心的consul客户端
├── consul-user-service -- 注册到consul的提供User对象CRUD接口的服务
└── consul-service -- 注册到consul的ribbon服务调用测试服务
```

- [Spring Cloud Gateway：新一代API网关服务](https://juejin.cn/post/6844903960696848397)

``` lua
springcloud-learning
├── eureka-server -- eureka注册中心
├── user-service -- 提供User对象CRUD接口的服务
└── api-gateway -- gateway作为网关的测试服务
```

- [Spring Boot Admin：微服务应用监控](https://juejin.cn/post/6844903984109617165)

``` lua
springcloud-learning
├── eureka-server -- eureka注册中心
├── admin-server -- admin监控中心服务
├── admin-client -- admin监控中心监控的应用服务
└── admin-security-server -- 带登录认证的admin监控中心服务
```

### Spring Cloud Oauth2

- [OAuth 2.0 的四种方式](https://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html)

- [Spring Cloud Security：Oauth2使用入门](https://juejin.cn/post/6844903987137740813)

``` lua
springcloud-learning
└── oauth2-server -- oauth2认证测试服务
```

- [Spring Cloud Security：Oauth2结合JWT使用](https://juejin.cn/post/6844903988727382024)

``` lua
springcloud-learning
└── oauth2-jwt-server -- 使用jwt的oauth2认证测试服务
```

- [Spring Cloud Security：Oauth2实现单点登录](https://juejin.cn/post/6844903992204623879)

``` lua
springcloud-learning
├── oauth2-jwt-server -- 使用jwt的oauth2认证测试服务
└── oauth2-client -- 单点登录的oauth2客户端服务
```

### Spring Cloud Alibaba

- [Spring Cloud Alibaba：Nacos 作为注册中心和配置中心使用](https://juejin.cn/post/6844903993873793032)

``` lua
springcloud-learning
├── nacos-user-service -- 注册到nacos的提供User对象CRUD接口的服务
├── nacos-ribbon-service -- 注册到nacos的ribbon服务调用测试服务
└── nacos-config-client -- 用于演示nacos作为配置中心的nacos客户端
```

- [Spring Cloud Alibaba：Sentinel实现熔断与限流](https://juejin.cn/post/6844903999876022279)

``` lua
springcloud-learning
├── sentinel-service -- sentinel功能测试服务
└── nacos-user-service -- 注册到nacos的提供User对象CRUD接口的服务
```

- [使用Seata彻底解决Spring Cloud中的分布式事务问题](https://juejin.cn/post/6844904001528397831)

``` lua
springcloud-learning
├── seata-order-service -- 整合了seata的订单服务
├── seata-storage-service -- 整合了seata的库存服务
└── seata-account-service -- 整合了seata的账户服务
```

### 微服务解决方案

- [微服务权限终极解决方案，Spring Cloud Gateway + Oauth2 实现统一认证和鉴权！](https://www.macrozheng.com/cloud/gateway_oauth2.html)
- [微服务聚合Swagger文档，这波操作是真的香！](https://www.macrozheng.com/cloud/knife4j_cloud.html)
- [我扒了半天源码，终于找到了Oauth2自定义处理结果的最佳方案！](https://www.macrozheng.com/cloud/oauth2_custom.html)
- [开箱即用！看看人家的微服务权限解决方案，那叫一个优雅！](https://www.macrozheng.com/cloud/sa_token_cloud_start.html)
- [再见Feign！推荐一款微服务间调用神器，跟SpringCloud绝配！](https://www.macrozheng.com/cloud/retrofit_cloud.html)


