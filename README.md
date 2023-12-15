# springcloud-learning

## 简介

Spring Cloud 教程，版本为 SpringBoot 2.1.7、Spring Cloud Greenwich、Spring Cloud Alibaba 2.1.0.RELEASE

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

- [Spring Cloud LoadBalancer：替代 Ribbon 负载均衡](https://juejin.cn/post/6844903984109617165)

``` lua
springcloud-learning
├── nacos-loadbalancer-service -- 注册到nacos的loadbalancer服务调用测试服务
└── nacos-user-service -- 注册到nacos的提供User对象CRUD接口的服务
```

### Spring Cloud Alibaba

- [Spring Cloud Alibaba：Nacos 作为注册中心和配置中心使用](https://juejin.cn/post/6844903993873793032)

``` lua
springcloud-learning
├── nacos-user-service -- 注册到nacos的提供User对象CRUD接口的服务
├── nacos-ribbon-service -- 注册到nacos的ribbon服务调用测试服务
└── nacos-config-client -- 用于演示nacos作为配置中心的nacos客户端
```

- [Spring Cloud Alibaba：Sentinel 实现熔断与限流](https://juejin.cn/post/6844903999876022279)

``` lua
springcloud-learning
├── sentinel-service -- sentinel功能测试服务
└── nacos-user-service -- 注册到nacos的提供User对象CRUD接口的服务
```

- [Spring Cloud Alibaba：Seata 解决分布式事务问题](https://juejin.cn/post/6844904001528397831)

``` lua
springcloud-learning
├── seata-order-service -- 整合了seata的订单服务
├── seata-storage-service -- 整合了seata的库存服务
└── seata-account-service -- 整合了seata的账户服务
```

### Spring Cloud Oauth2

- [OAuth 2.0 的四种方式](https://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html)
- [Spring Cloud Security：Oauth2 使用入门](https://juejin.cn/post/6844903987137740813)

``` lua
springcloud-learning
└── oauth2-server -- oauth2认证测试服务
```

- [Spring Cloud Security：Oauth2 + JWT 使用](https://juejin.cn/post/6844903988727382024)

``` lua
springcloud-learning
└── oauth2-jwt-server -- 使用jwt的oauth2认证测试服务
```

- [Spring Cloud Security：Oauth2 实现单点登录](https://juejin.cn/post/6844903992204623879)

``` lua
springcloud-learning
├── oauth2-jwt-server -- 使用jwt的oauth2认证测试服务
└── oauth2-client -- 单点登录的oauth2客户端服务
```

### 微服务解决方案

- [Spring Cloud Gateway + Oauth2 实现统一认证和鉴权](https://juejin.cn/post/6850037263707930631)
- [Spring Cloud Gateway + Oauth2 自定义处理结果](https://juejin.cn/post/6857296054392471559)
- [Spring Cloud Gateway + Oauth2 依赖版本升级](https://juejin.cn/post/7121892567130013732)

``` lua
springcloud-learning
├── micro-oauth2-gateway -- 网关服务，负责请求转发和鉴权功能，整合Spring Security、Oauth2
├── micro-oauth2-auth -- Oauth2认证服务，负责对登录用户进行认证，整合Spring Security、Oauth2
└── micro-oauth2-api -- 受保护的API服务，用户鉴权通过后可以访问该服务，不整合Spring Security、Oauth2
```

- [Sa-Token 权限认证](https://sa-token.cc/doc.html#/use/jur-auth)
- [Spring Cloud Gateway + Sa-Token 微服务权限解决方案](https://juejin.cn/post/7003141949259513887)

``` lua
springcloud-learning
├── micro-sa-token-gateway：网关服务，负责请求转发、登录认证和权限认证
├── micro-sa-token-auth：认证服务，仅包含一个登录接口，调用Sa-Token的API实现
├── micro-sa-token-api：受保护的API服务，用户通过网关鉴权通过后可以访问该服务
└── micro-sa-token-common：通用工具包，抽取其他服务公用的类
```

- [knife4j 聚合微服务 Swagger 文档](https://juejin.cn/post/6854573219916201997)

``` lua
springcloud-learning
├── micro-knife4j-gateway：网关服务，作为微服务API文档的访问入口，聚合所有API文档，需要引入文档前端UI包
├── micro-knife4j-user：用户服务，普通API服务，不需要引入文档前端UI包
└── micro-knife4j-order：订单服务，普通API服务，不需要引入文档前端UI包
```

