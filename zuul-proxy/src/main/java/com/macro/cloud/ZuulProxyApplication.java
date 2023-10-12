package com.macro.cloud;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.zuul.FilterProcessor;
import com.netflix.zuul.http.ZuulServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.ZuulServerAutoConfiguration;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.cloud.netflix.zuul.web.ZuulController;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

@EnableZuulProxy // 启用Zuul的API网关功能
@EnableDiscoveryClient
@SpringBootApplication
public class ZuulProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulProxyApplication.class, args);
    }

    /**
     * ZuulServlet 对比 DispatcherServlet
     *
     * 1.配置初始化
     *
     * 通过 ServletRegistrationBean 注册 Servlet
     * @see DispatcherServletAutoConfiguration.DispatcherServletRegistrationConfiguration#dispatcherServletRegistration(org.springframework.web.servlet.DispatcherServlet)
     * @see ZuulServerAutoConfiguration#zuulServlet()
     *
     * ZuulServlet UrlMappings = '/zuul/*'
     * DispatcherServlet UrlMappings = '/'
     *
     *
     * 2.接收请求，分为两种情况：
     *
     * 对于 '/' 的请求，会经过以下路径达到 ZuulServlet
     * @see DispatcherServlet#doService(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     * @see ZuulController#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     * @see ZuulServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     *
     * 对于 '/zuul/*' 的请求，会直接到达 ZuulServlet
     * @see ZuulServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     *
     *
     * 3.结论
     *
     * 一般情况下，ZuulServet 被嵌入到 Spring Dispatch 机制中，由 DispatcherServlet 分派处理，这样 Spring MVC 可以控制路由，并且 Zuul 缓冲请求。
     * 如果需要绕过 DispatcherServlet 的 multipart 处理，在不缓冲请求的情况下通过 Zuul(例如，对于大文件上传)，ZuulServlet 也可以装载在 Spring Dispatcher 之外，让请求绕过 DispatcherServlet。
     * @see DispatcherServlet#checkMultipart(javax.servlet.http.HttpServletRequest)
     * @see MultipartResolver#resolveMultipart(javax.servlet.http.HttpServletRequest)
     */

    /**
     * Zuul1 设计比较简单，代码不多也比较容易读懂，它本质上就是一个同步 Servlet，采用多线程阻塞模型。
     * https://www.jianshu.com/p/486e70e8f100
     *
     * 同步 Servlet 使用 thread per connection 方式处理请求。
     * 简单讲，每来一个请求，Servlet 容器要为该请求分配一个线程专门负责处理这个请求，直到响应返回客户端这个线程才会被释放返回容器线程池。
     * 如果后台服务调用比较耗时，那么这个线程就会被阻塞，阻塞期间线程资源被占用，不能干其它事情。
     * 同步阻塞模式一般会启动很多的线程，必然引入线程切换开销。
     * 另外，Servlet 容器线程池的大小是有限制的，当前端请求量大，而后台慢服务比较多时，很容易耗尽容器线程池内的线程，造成容器无法接受新的请求。
     * Netflix 为此还专门研发了 Hystrix 熔断组件来解决慢服务耗尽资源问题。
     *
     * 对后台服务调用也是同步阻塞的。
     * 经验证，跟 Servlet 使用的是同一个线程，并不是另起 IO 线程。
     * 根据源码注释，这里是 Used for synchronous execution of command.
     * @see FilterProcessor#route()
     * @see RibbonRoutingFilter#forward(org.springframework.cloud.netflix.ribbon.support.RibbonCommandContext)
     * @see HystrixCommand#execute()
     */
}
