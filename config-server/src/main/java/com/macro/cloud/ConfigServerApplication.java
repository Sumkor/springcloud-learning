package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Spring Cloud Config 分为服务端和客户端两个部分。
 * 服务端被称为分布式配置中心，它是个独立的应用，可以从 Git 配置仓库获取配置信息并提供给客户端使用。
 * 客户端可以通过配置中心来获取配置信息，在启动时加载配置。
 */
@EnableConfigServer // 启用配置中心功能
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigServerApplication {

    /**
     * Spring Cloud Config 有它的一套访问规则：
     *
     * /{application}/{profile}[/{label}]
     * /{application}-{profile}.yml
     * /{application}-{profile}.properties
     * /{label}/{application}-{profile}.yml
     * /{label}/{application}-{profile}.properties
     *
     * application：代表应用名称，默认为配置文件中的 spring.application.name，如果配置了 spring.cloud.config.name，则为该名称；
     * label：代表分支名称，对应配置文件中的 spring.cloud.config.label；
     * profile：代表环境名称，对应配置文件中的 spring.cloud.config.profile。
     *
     * 访问 http://localhost:8901/master/config-dev 来获取 master 分支上 config-dev.yml 文件的信息
     * 访问 http://localhost:8901/master/config-dev.yml 来获取 master 分支上 config-dev.yml 文件的内容
     */
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

}
