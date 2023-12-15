package com.macro.cloud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate相关配置
 * Created by macro on 2019/8/29.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    @ConfigurationProperties(prefix = "rest.template.config")
    public HttpComponentsClientHttpRequestFactory customHttpRequestFactory() {
        // 支持多种配置选项，如连接超时、读取超时、连接池等，用于管理HTTP客户端请求
        return new HttpComponentsClientHttpRequestFactory();
    }

    @Bean
    @LoadBalanced // 赋予其负载均衡能力
    public RestTemplate restTemplate() {
        return new RestTemplate(customHttpRequestFactory());
    }
}
