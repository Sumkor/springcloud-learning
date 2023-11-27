package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 这里我们会创建三个服务，一个订单服务，一个库存服务，一个账户服务。
 * 当用户下单时，会在订单服务中创建一个订单，然后通过远程调用库存服务来扣减下单商品的库存，
 * 再通过远程调用账户服务来扣减用户账户里面的余额，最后在订单服务中修改订单状态为已完成。
 * 该操作跨越三个数据库，有两次远程调用，很明显会有分布式事务问题。
 *
 * Seata 是一个开源的分布式事务解决方案，可以解决跨数据库事务问题。
 *
 * https://github.com/seata/seata/releases/download/v0.8.0/seata-server-0.8.0.zip
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients
public class SeataOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataOrderServiceApplication.class, args);
    }

    /**
     * 协议分布式事务处理过程的三个组件：TM（Transaction Manager），RM（Resource Manager）和 TC（Transaction Coordinator）。
     *
     * TC 负责维护全局事务的状态，负责协调并驱动全局事务的提交或回滚。（可以理解为 Seata Server）
     * TM 负责开启一个全局事务，并最终发起全局提交或全局回滚的决议。（可以理解为全局调用的发起者，哪个服务发起了全局服务调用，这个服务就是 TM）
     * RM 负责维护分支事务的状态，并负责分支事务的提交和回滚。（可以理解为整个全局调用中，每个节点都是一个 RM）
     *
     * 一个典型的分布式事务过程
     *
     * TM 向 TC 申请开启一个全局事务，全局事务创建成功并生成一个全局唯一的 XID 并在调用链路的上下文中传播；
     * RM 向 TC 注册分支事务，将其纳入 XID 对应全局事务的管辖；
     * TM 向 TC 发起针对 XID 的全局提交或回滚决议；
     * TC 调度 XID 下管辖的全部分支事务完成提交或回滚请求。
     */
}
