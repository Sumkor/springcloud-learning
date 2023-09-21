package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka基于AP（可用性、分区可容错性）， 注重服务的可用性，即使所有机器都挂了，也能拿到本地缓存的数据，保证高可用。
 * Zookeeper基于CP（一致性、分区可容错性）， 注重数据的一致性，若主机挂掉则zk集群整体不对外提供服务了，需要选一个新leader的出来（120s左右）才能继续对外提供服务，不保证高可用。
 * https://blog.csdn.net/liugw_768/article/details/127336738
 */
@EnableEurekaServer // 启用Eureka注册中心功能
@SpringBootApplication
public class EurekaServerApplication {

    /**
     * http://localhost:8001/
     */
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

}
