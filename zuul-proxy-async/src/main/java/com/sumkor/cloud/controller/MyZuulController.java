package com.sumkor.cloud.controller;

import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.web.ZuulController;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自定义 ZuulController，使用 Servlet 3.0 异步响应
 * 需要修改自动配置，替换掉原生的 ZuulController
 *
 * @author Sumkor
 * @since 2023/10/12
 */
public class MyZuulController extends ZuulController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 默认请求最长超时时间，这里和tomcat中采用一样的参数 20000ms
     */
    private static final String DEFAULT_REQUEST_TIME_OUT = "20000";
    private static final ExecutorService asyncTaskExecutor = Executors.newFixedThreadPool(10);

    /**
     * http://localhost:8802/userService/user/1
     *
     * 从调用者(浏览器)的角度而言，是感知不到有什么变化的，因为都是得等待5s才返回数据。
     * 但是，从服务端的日志我们可以看出，请求是直接返回的(而不是像SpringMVC一直同步阻塞5s，线程才返回)。
     *
     * 2023-10-19 10:52:00.559  INFO 22556 --- [nio-8802-exec-1] c.s.cloud.controller.MyZuulController    : 请求开始...
     * 2023-10-19 10:52:00.559  INFO 22556 --- [nio-8802-exec-1] c.s.cloud.controller.MyZuulController    : 请求结束...
     * 2023-10-19 10:52:00.559  INFO 22556 --- [pool-1-thread-1] c.s.cloud.controller.MyZuulController    : 开始执行异步任务...
     * 2023-10-19 10:52:01.905  INFO 22556 --- [pool-1-thread-1] c.s.cloud.controller.MyZuulController    : 结束执行异步任务...
     */
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
        // 对于Multipart类型的请求，异步处理会导致临时文件丢失，暂时交回给tomcat连接线程处理
        if (request instanceof StandardMultipartHttpServletRequest) {
            try {
                return super.handleRequestInternal(request, response);
            } catch (Exception e) {
                log.error("zuul request failed", e);
            } finally {
                RequestContext.getCurrentContext().unset();
            }
        }

        final AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(Long.parseLong(DEFAULT_REQUEST_TIME_OUT));

        log.info("请求开始...");
        asyncTaskExecutor.execute(() -> {
            try {
                log.info("开始执行异步任务...");
                Thread.sleep(5000);
                super.handleRequestInternal((HttpServletRequest) asyncContext.getRequest(),
                        (HttpServletResponse) asyncContext.getResponse());
                log.info("结束执行异步任务...");
            } catch (Exception e) {
                log.error("zuul request failed", e);
            } finally {
                try {
                    asyncContext.complete();
                } catch (IllegalStateException ignored) {
                }
                RequestContext.getCurrentContext().unset();
            }
        });
        log.info("请求结束...");
        // 这个返回null，但是对应的response不会返回null的
        return null;
    }
}
