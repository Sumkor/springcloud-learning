package com.macro.cloud;

import com.macro.cloud.config.RibbonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

@EnableDiscoveryClient
@SpringBootApplication
@RibbonClients(defaultConfiguration = RibbonConfig.class) // 在主启动类上配置，默认应用于所有的客户端。
public class RibbonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonServiceApplication.class, args);
    }

}
