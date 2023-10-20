package com.macro.cloud.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * Spring Cloud Gateway 自带了一个限流实现，就是 RedisRateLimiter，可以用于分布式限流。
 * 基于令牌桶算法，不依赖于内部线程，而是在每次处理请求之前先实时计算出要填充的令牌数并填充，然后再从桶中获取令牌。
 * 具体逻辑是放在一段 lua 脚本中的：src/main/resources/META-INF/scripts/request_rate_limiter.lua
 *
 * 令牌桶算法（Token Bucket）是目前应用最广泛的一种限流算法，它的基本思想由两部分组成：生成令牌 和 消费令牌。
 * 生成令牌：假设有一个装令牌的桶，最多能装 M 个，然后按某个固定的速度（每秒 r 个）往桶中放入令牌，桶满时不再放入；
 * 消费令牌：我们的每次请求都需要从桶中拿一个令牌才能放行，当桶中没有令牌时即触发限流，这时可以将请求放入一个缓冲队列中排队等待，或者直接拒绝；
 * https://www.aneasystone.com/archives/2020/08/spring-cloud-gateway-current-limiting.html
 *
 * Created by macro on 2019/9/25.
 * @see RedisRateLimiter
 */
@Configuration
public class RedisRateLimiterConfig {

    /**
     * 根据请求参数中的username进行限流
     */
    //@Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("username"));
    }

    /**
     * 根据访问IP进行限流
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    /**
     * 配置限流策略
     *
     * Spring Cloud Gateway 自带的限流器有一个很大的坑，replenishRate 不支持设置小数，也就是说往桶中填充的 token 的速度最少为每秒 1 个，
     * 所以，如果限流规则是每分钟 10 个请求（按理说应该每 6 秒填充一次，或每秒填充 1/6 个 token），这种情况 Spring Cloud Gateway 就没法正确的限流
     */
    //@Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .method("get")
                        .filters(filter -> filter.requestRateLimiter()
                                .rateLimiter(RedisRateLimiter.class, rl -> rl.setBurstCapacity(3).setReplenishRate(1)).and())
                        .uri("http://localhost:8201"))
                .build();
    }
}
