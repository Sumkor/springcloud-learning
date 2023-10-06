package com.macro.cloud;

import com.netflix.zuul.http.ZuulServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.ZuulServerAutoConfiguration;
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

}
