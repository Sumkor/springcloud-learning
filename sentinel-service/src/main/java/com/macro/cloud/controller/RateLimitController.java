package com.macro.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.macro.cloud.domain.CommonResult;
import com.macro.cloud.handler.CustomBlockHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 限流功能
 * Created by macro on 2019/11/7.
 */
@RestController
@RequestMapping("/rateLimit")
public class RateLimitController {

    /**
     * http://localhost:8401/rateLimit/byResource
     * 按资源名称限流（在 dashboard 中配置资源名为 byResource），必须指定限流处理逻辑 blockHandler
     */
    @GetMapping("/byResource")
    @SentinelResource(value = "byResource", blockHandler = "handleException")
    public CommonResult byResource() {
        return new CommonResult("按资源名称限流", 200);
    }

    /**
     * http://localhost:8401/rateLimit/byUrl
     * 按 URL 限流（在 dashboard 中配置资源名为 /rateLimit/byUrl），有默认的限流处理逻辑
     */
    @GetMapping("/byUrl")
    @SentinelResource(value = "byUrl"/*, blockHandler = "handleException"*/)
    public CommonResult byUrl() {
        return new CommonResult("按url限流", 200);
    }

    /**
     * 自定义限流处理逻辑
     *
     * blockHandler：指定流量控制之后，默认的处理方法名称
     * blockHandlerClass：包含 blockHandler 方法的类
     */
    @GetMapping("/customBlockHandler")
    @SentinelResource(value = "customBlockHandler", blockHandler = "handleException", blockHandlerClass = CustomBlockHandler.class)
    public CommonResult blockHandler() {
        return new CommonResult("限流成功", 200);
    }

    /**
     * 处理 BlockException 的方法
     */
    public CommonResult handleException(BlockException exception) {
        return new CommonResult(exception.getClass().getCanonicalName(), 200);
    }

}
