package com.macro.cloud.config;

import com.macro.cloud.annotation.MyQualifier;
import com.netflix.loadbalancer.*;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.context.named.NamedContextFactory;
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
     * 更改负载均衡策略（使用 RestTemplate 的时候，在 yml 中配置是无效的，需要在此配置）
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
    public IRule myRule() {
        RandomRule randomRule = new RandomRule();
        return randomRule;
    }

    /**
     * 远程服务第一次访问成功，后续访问失败问题。
     *
     * loadBalancer 实例中记录了远程服务列表，会定时更新远程服务列表
     * @see DynamicServerListLoadBalancer#updateListOfServers()
     *
     * 注意！这里使用了 {@link #myRule} 自定义了负载均衡算法，与 zipkin 组件整合的时候，需要注意：
     * 启动类上必须要加注解 @RibbonClients(defaultConfiguration = RibbonConfig.class)
     * 否则会出现多个 loadBalancer 实例共享同一个 rule 实例，由于 loadBalancer 实例与 rule 实例是互相引用的，这样就串数据了！
     * 导致从 rule 实例中获取到错误的 loadBalancer 实例，因此拿不到远程服务列表而访问失败！
     * @see RandomRule#choose(java.lang.Object)
     *
     * loadBalancer 实例与 rule 实例之间互相引用！
     * @see BaseLoadBalancer#setRule
     * @see AbstractLoadBalancerRule#setLoadBalancer
     *
     */

    /**
     * 在 Ribbon 中，LoadBalancer 和 Rule 是两个关键的概念，它们分别负责负载均衡和选择负载均衡策略的工作。
     *
     *     LoadBalancer（负载均衡器）：
     *     LoadBalancer负责将来自客户端的请求分发到服务实例上，以实现负载均衡。它通过维护一个可用服务实例的列表，并根据选定的负载均衡策略选择要发送请求的实例。Ribbon提供了默认的LoadBalancer实现，但也可以自定义实现。
     *
     *     Rule（规则）：
     *     Rule定义了负载均衡的策略或规则，用于选择要从LoadBalancer中选择的服务实例。Ribbon提供了一些默认的规则，例如RoundRobinRule（轮询）、RandomRule（随机）等，也可以自定义规则。
     *
     * 总结来说，LoadBalancer 负责将请求分发到服务实例，而 Rule 决定了如何选择服务实例。
     */

    /**
     * @RibbonClients 实现原理
     *
     * `@RibbonClients` 注解是用于在主启动类上配置全局的 Ribbon 客户端的，实现对全局和特定客户端的 Ribbon 配置的统一管理。
     * `@RibbonClients` 注解的实现原理是通过 Spring 的注解处理器和 BeanDefinitionRegistry 来注册 Ribbon 客户端配置。
     * 具体来说：
     *  - 当使用 `@RibbonClients` 注解时，Spring 在启动时会扫描所有的类，并检测到带有 `@RibbonClients` 注解的类。
     *  - 然后，注解处理器会解析该注解，提取出其中的配置信息。
     *    - 首先会解析 `defaultConfiguration` 属性，该属性指定了默认的 Ribbon 客户端配置类。
     *    - 然后，根据 `@RibbonClient` 注解的信息，逐个注册每个客户端的配置类。
     *  - 注册过程中，会将配置类的信息转化为 BeanDefinition，并将其添加到 BeanDefinitionRegistry 中，以便 Spring 在后续的实例化和依赖注入过程中能够正确地创建和配置 Ribbon 客户端。
     */

    /**
     * 重大发现，获取 user-service 实例的时候，会创建子容器！
     *
     * 这里入参 name="user-service" type=ILoadBalancer.class
     * @see NamedContextFactory#getInstance(java.lang.String, java.lang.Class)
     *
     * 1. 当主启动类 RibbonServiceApplication 上标记了 @RibbonClients(defaultConfiguration = RibbonConfig.class)
     *
     * 创建子容器过程，具有以下几个配置类：
     *     default.org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration
     *     default.org.springframework.cloud.netflix.ribbon.eureka.RibbonEurekaAutoConfiguration
     *     default.com.macro.cloud.RibbonServiceApplication
     * @see NamedContextFactory#createContext(java.lang.String)
     *
     * 此时，LoadBalancer 实例是在子容器中，而在子容器中，通过 default.com.macro.cloud.RibbonServiceApplication 创建了一个 Rule 实例，它们之间互相引用。
     *
     * 2. 当主启动类 RibbonServiceApplication 上没有标记 @RibbonClients(defaultConfiguration = RibbonConfig.class)
     *
     * 创建子容器过程，具有以下几个配置类：
     *     default.org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration
     *     default.org.springframework.cloud.netflix.ribbon.eureka.RibbonEurekaAutoConfiguration
     * @see NamedContextFactory#createContext(java.lang.String)
     *
     * 此时，LoadBalancer 实例是在子容器中，而 Rule 实例是在父容器中，就会导致多个 LoadBalancer 实例绑定同一个 Rule 实例而出错！
     */
}
