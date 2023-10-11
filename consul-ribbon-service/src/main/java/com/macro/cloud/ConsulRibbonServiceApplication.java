package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 本例中，将 Consul 作为注册中心使用。
 *
 * Consul 采用的是 CP （Consistency-Partition Tolerance） 模型。
 * CP 模型确保在面对网络分区的情况下，即使在节点之间的通信出现问题，Consul 仍然能够保持一致性。
 * 它通过使用 Raft 一致性算法来达到这个目标，通过复制日志来在节点之间实现一致性。
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ConsulRibbonServiceApplication {

    /**
     * Consul 是 HashiCorp 公司推出的开源软件，提供了微服务系统中的服务治理、配置中心、控制总线等功能。
     *
     * Spring Cloud Consul 具有如下特性：
     *
     * 支持服务治理：Consul作为注册中心时，微服务中的应用可以向 Consul 注册自己，并且可以从 Consul 获取其他应用信息；
     * 支持客户端负责均衡：包括 Ribbon 和 Spring Cloud LoadBalancer；
     * 支持 Zuul：当 Zuul 作为网关时，可以从 Consul 中注册和发现应用；
     * 支持分布式配置管理：Consul 作为配置中心时，使用键值对来存储配置信息；
     * 支持控制总线：可以在整个微服务系统中通过 Control Bus 分发事件消息。
     */
    public static void main(String[] args) {
        SpringApplication.run(ConsulRibbonServiceApplication.class, args);
    }

}
