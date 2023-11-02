package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class SentinelServiceApplication {

    /**
     * 默认情况下，当我们在 Sentinel 控制台中配置规则时，控制台是通过 API 将规则推送至客户端并直接更新到内存中。
     * 一旦我们重启应用，规则将消失。
     *
     * 可以将规则进行持久化到 Nacos：
     * 1.在 Nacos 创建规则并发布，可以将规则推送到 Sentinel 控制台和客户端。
     * 2.在 Sentinel 控制台可以从 Nacos 获取配置信息。
     * 3.在 Sentinel 修改规则过后不能直接同步到 Nacos 中进行持久化，但是可以推送到客户端。
     */
    public static void main(String[] args) {
        SpringApplication.run(SentinelServiceApplication.class, args);
    }

}
