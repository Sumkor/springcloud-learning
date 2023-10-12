package com.sumkor.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.ZuulProxyAutoConfiguration;
import org.springframework.cloud.netflix.zuul.ZuulServerAutoConfiguration;

/**
 * zuul + servlet 3.0
 *
 * @author Sumkor
 * @since 2023/10/12
 */
@EnableZuulProxy // 启用Zuul的API网关功能
@EnableDiscoveryClient
@SpringBootApplication(exclude = {ZuulProxyAutoConfiguration.class, ZuulServerAutoConfiguration.class})
public class ZuulProxyAsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulProxyAsyncApplication.class, args);
    }
}
