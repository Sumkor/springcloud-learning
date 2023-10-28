package com.macro.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by macro on 2019/9/11.
 */
@RestController
@RefreshScope
public class ConfigClientController {

    /**
     * http://localhost:9101/configInfo
     *
     * ${spring.application.name}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
     *
     * 应用名称为 nacos-config-client 的应用在 dev 环境下的 yaml 配置，dataid 为 nacos-config-client-dev.yaml，配置内容为：
     * config:
     *   info: "config info for dev"
     */
    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return configInfo;
    }
}
