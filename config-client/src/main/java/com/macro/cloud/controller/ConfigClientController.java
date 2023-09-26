package com.macro.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by macro on 2019/9/11.
 */
@RestController
@RefreshScope // 用于刷新配置
public class ConfigClientController {

    /**
     * 读取远程配置文件中指定节点的内容
     */
    @Value("${config.info}")
    private String configInfo;

    /**
     * 访问 http://localhost:9001/configInfo，获取到远程仓库指定分支下配置文件内容
     */
    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return configInfo;
    }
}
