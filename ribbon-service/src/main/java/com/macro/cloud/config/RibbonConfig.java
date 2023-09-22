package com.macro.cloud.config;

import com.macro.cloud.annotation.MyQualifier;
import com.netflix.loadbalancer.*;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by macro on 2019/8/29.
 */
@Configuration
public class RibbonConfig {

    /**
     * 在负载均衡模式下，SpringCloud 会使用 {@link LoadBalancerClient} 对 RestTemplate 发起的请求进行处理。
     */
    @Bean
    @LoadBalanced // 开启负载均衡能力
    public RestTemplate restTemplate() {
        return new RestTemplate();
        /**
         * 实现原理：
         * https://zhuanlan.zhihu.com/p/617967367
         *
         * 在 @LoadBalanced 中使用 @Qualifier 作为元注解，相当于是一个自定义的 @Qualifier 注解。
         * 它用在 LoadBalancerAutoConfiguration 中，用于注入特定的 RestTemplate 实例。
         * @see LoadBalancerAutoConfiguration#loadBalancedRestTemplateInitializerDeprecated
         *
         * LoadBalancerAutoConfiguration 注入 RestTemplate 之后，使用 RestTemplateCustomizer 来装配它。
         * 具体就是给 RestTemplate 加上拦截器 LoadBalancerInterceptor
         * @see LoadBalancerAutoConfiguration.LoadBalancerInterceptorConfig
         *
         * 使用当前 RestTemplate 发送请求的时候，会进入拦截器
         * @see LoadBalancerInterceptor#intercept
         * @see RibbonLoadBalancerClient#execute(java.lang.String, org.springframework.cloud.client.loadbalancer.LoadBalancerRequest, java.lang.Object)
         *
         * 默认是采用 ZoneAwareLoadBalancer 作为负载均衡策略，复合判断服务所在区域的性能和服务的可用性进行轮询选择
         * @see RibbonLoadBalancerClient#getServer(com.netflix.loadbalancer.ILoadBalancer, java.lang.Object)
         * @see ZoneAwareLoadBalancer#chooseServer(java.lang.Object)
         */
    }

    @Bean
    @MyQualifier // 自定义注解
    public RestTemplate myRestTemplate() {
        return new RestTemplate();
    }

    /**
     * 更改负载均衡策略
     *
     * Ribbon中提供了多个内置的负载均衡算法，下面介绍每个算法的实现原理（by ChatGPT 3.5）：
     *
     * 1. RoundRobinRule（轮询算法）：
     *    - 实现原理：采用简单的轮询方式，依次选择可用的服务实例进行请求转发。
     *    - Ribbon会维护一个计数器，每次选择下一个服务实例时递增计数器的值，达到轮询的效果。
     *
     * 2. RandomRule（随机算法）：
     *    - 实现原理：随机选择一个可用的服务实例进行请求转发。
     *    - Ribbon使用Java的`java.util.Random`类生成一个随机数，根据随机数在可用的服务实例列表中进行选择。
     *
     * 3. BestAvailableRule（最佳可用算法）：
     *    - 实现原理：选择具备最佳性能的服务实例进行请求转发。
     *    - Ribbon会根据服务实例的健康状态和连接数等指标，选择具备最佳性能的实例，例如，选择连接数最少的实例。
     *
     * 4. WeightedResponseTimeRule（加权响应时间算法）：
     *    - 实现原理：根据服务实例的平均响应时间和权重，进行加权选择。
     *    - Ribbon会根据服务实例的平均响应时间进行加权计算，响应时间越长的实例权重越低，选择权重较高的实例进行请求转发。
     *
     * 5. AvailabilityFilteringRule（可用性过滤算法）：
     *    - 实现原理：根据服务实例的可用性和可用区域进行过滤和选择。
     *    - Ribbon会先过滤掉不可用的服务实例，再根据可用区域选择合适的实例进行请求转发。
     *
     * 6. ZoneAvoidanceRule（区域避免算法）：
     *    - 实现原理：根据服务实例所在的区域进行避免跨区域调用。
     *    - Ribbon会尽量选择在同一区域内的实例，避免跨区域调用，以降低延迟和故障风险。
     *
     * LoadBalancerClient 在执行请求的时候，会记录服务的连接数{@link ServerStats#incrementActiveRequestsCount()}、响应时间{@link ServerStats#getResponseTimeAvg()}等信息，用于下一次负载均衡
     * @see RibbonLoadBalancerClient#execute(java.lang.String, org.springframework.cloud.client.ServiceInstance, org.springframework.cloud.client.loadbalancer.LoadBalancerRequest)
     */
    @Bean
    public IRule myRule(){
        return new RandomRule();
    }
}
