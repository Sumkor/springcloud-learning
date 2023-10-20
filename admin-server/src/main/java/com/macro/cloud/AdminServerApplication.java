package com.macro.cloud;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SpringBoot 应用可以通过 Actuator 来暴露应用运行过程中的各项指标，SpringBoot Admin 通过这些指标来监控 SpringBoot 应用，然后通过图形化界面呈现出来。
 * SpringBoot Admin 不仅可以监控单体应用，还可以和 Spring Cloud 的注册中心相结合来监控微服务应用。
 * SpringBoot Admin 可以提供应用的以下监控信息：
 *
 * 监控应用运行过程中的概览信息；
 * 度量指标信息，比如 JVM、Tomcat 及进程信息；
 * 环境变量信息，比如系统属性、系统环境变量以及应用配置信息；
 * 查看所有创建的 Bean 信息；
 * 查看应用中的所有配置信息；
 * 查看应用运行日志信息；
 * 查看 JVM 信息；
 * 查看可以访问的Web端点；
 * 查看 HTTP 跟踪信息。
 */
@EnableDiscoveryClient // 使用注册中心时开启
@EnableAdminServer
@SpringBootApplication
public class AdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }

}
