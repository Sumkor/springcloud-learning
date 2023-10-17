package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Cloud Gateway 具有如下特性：
 *
 * 基于 Spring Framework 5, Project Reactor 和 Spring Boot 2.0 进行构建；
 * 动态路由：能够匹配任何请求属性；
 * 可以对路由指定 Predicate（断言）和 Filter（过滤器）；
 * 集成 Hystrix 的断路器功能；
 * 集成 Spring Cloud 服务发现功能；
 * 易于编写的 Predicate（断言）和 Filter（过滤器）；
 * 请求限流功能；
 * 支持路径重写。
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    /**
     * Gateway VS Zuul，据 Spring Cloud Gateway 原作者的解释：
     * https://stackoverflow.com/questions/47092048/how-is-spring-cloud-gateway-different-from-zuul
     *
     * Zuul 构建于 Servlet 2.5，兼容 3.x，使用的是阻塞式的 API，不支持长连接，比如 websockets。
     * Zuul 2.x 在底层上有了很大的改变，使用了异步无阻塞式的 API，性能改善明显。
     * Spring Cloud Gateway 构建于 Spring 5+，基于 Spring Boot 2.x 响应式的、非阻塞式的 API。同时，它支持 websockets，和 Spring 框架紧密集成，对开发者友好。
     */

    /**
     * 关于响应式编程的讨论：
     *
     * 响应式编程，假设我有一个请求是请求数据库的，我请求我们的应用，应用支持响应式异步，很快交给数据库请求，数据库请求响应式的，交给了数据库。
     * 那么问题来了，最终数据库要执行的时间是不会变的，在这次的请求中，我们的应用是快了，没有阻塞，可以处理更多的请求。
     * 但是数据库要请求处理的时间是一样的，那么压力都在数据库那里。
     *
     * 简单来说，这种就是把压力放到被调用方。被调用方还是要处理那么久。
     * 假设，我不用这种响应式，我用多线程处理，效果是类似的（但是会引入了线程切换开销和线程资源耗尽问题）。
     *
     * 在 Gateway 里面，使用的 httpclient 也是不一样的，也是支持异步的。
     * 所以说 Gateway 很快，是因为把压力转出去了，所以说 Reactor 这种响应式，配合做网关就是很完美了。
     * @see reactor.netty.http.client.HttpClient
     * @see org.springframework.web.reactive.function.client.WebClient
     */
}
