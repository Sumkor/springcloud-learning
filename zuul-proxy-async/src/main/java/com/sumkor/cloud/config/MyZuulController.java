package com.sumkor.cloud.config;

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

        asyncTaskExecutor.execute(() -> {
            try {
                log.info("等待执行异步任务...");
                Thread.sleep(1000);
                log.info("开始执行异步任务...");
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
        log.info("结束响应...");
        // 这个返回null，但是对应的response不会返回null的
        return null;
    }
}
