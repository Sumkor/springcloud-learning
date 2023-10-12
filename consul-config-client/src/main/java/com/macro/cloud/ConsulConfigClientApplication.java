package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 本例中，将 Consul 作为配置中心使用。
 *
 * 只要修改下 Consul 中的配置信息，再次调用查看配置的接口，就会发现配置已经刷新。
 * 回想下在使用 Spring Cloud Config 的时候，我们需要调用接口，通过 Spring Cloud Bus 才能刷新配置。
 * Consul 使用其自带的 Control Bus 实现了一种事件传递机制，从而实现了动态刷新功能。
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ConsulConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsulConfigClientApplication.class, args);
    }

}
