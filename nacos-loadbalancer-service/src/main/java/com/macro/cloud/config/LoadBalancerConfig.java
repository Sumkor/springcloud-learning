package com.macro.cloud.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;

/**
 * LoadBalancer相关配置
 * Created by macro on 2022/7/26.
 */
@Configuration
public class LoadBalancerConfig {

    /**
     * 通过 LoadBalancerRequestTransformer 对原始请求进行转换
     */
    @Bean
    public LoadBalancerRequestTransformer transformer() {
        return new LoadBalancerRequestTransformer() {
            @Override
            public HttpRequest transformRequest(HttpRequest request, ServiceInstance instance) {
                return new HttpRequestWrapper(request) {
                    @Override
                    public HttpHeaders getHeaders() {
                        // 在每次远程调用中传入自定义的请求头
                        HttpHeaders headers = new HttpHeaders();
                        headers.putAll(super.getHeaders());
                        headers.add("X-InstanceId", instance.getInstanceId());
                        return headers;
                    }
                };
            }
        };
    }
}
