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
         * @see BaseLoadBalancer#chooseServer(java.lang.Object)
         * @see PredicateBasedRule#choose(java.lang.Object)
         * @see AbstractServerPredicate#chooseRoundRobinAfterFiltering(java.util.List, java.lang.Object)
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
     * RoundRobinRule：轮询策略
     * RandomRule：随机策略
     * BestAvailableRule：最大可用策略，即先过滤出故障服务实例后，选择一个当前并发请求数最小的。
     * WeightedResponseTimeRule：响应时间权重策略，对各个服务实例响应时间进行加权处理，然后再采用轮询的方式选择一个。
     * AvailabilityFilteringRule：可用过滤策略，先过滤出有故障的或并发请求大于阈值的一部分服务实例，然后再采用轮询的方式选择一个。
     * ZoneAvoidanceRule：区域感知策略，先使用主过滤条件（区域负载器，选择最优区域）对所有实例过滤并返回过滤后的实例，然后再采用轮询的方式选择一个。
     */
    @Bean
    public IRule myRule(){
        return new RandomRule();
    }
}
