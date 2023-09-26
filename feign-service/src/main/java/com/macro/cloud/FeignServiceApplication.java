package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Spring Cloud OpenFeign 是声明式的服务调用工具
 *
 * 在 2020.x 版本之前，open feign 默认依赖 hystrix、ribbon。
 * 从 2020.x 版本开始，open feign 不再依赖 hystrix、ribbon。
 */
@EnableFeignClients // 启用Feign的客户端功能
@EnableDiscoveryClient
@SpringBootApplication
public class FeignServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignServiceApplication.class, args);
    }

}
