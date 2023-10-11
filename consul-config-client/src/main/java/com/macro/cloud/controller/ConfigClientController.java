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
     * 读取远程配置文件中指定节点的内容，需要事先在 Consul 服务器上配置信息。
     *
     * key 值为：config/consul-config-client:dev/data
     * value 值为：config.info: "config info for dev"
     *
     * key 值为：config/consul-config-client:test/data
     * value 值为：config.info: "config info for test"
     *
     * 当项目的 profile 为 dev 时，可以读到 key='config/consul-config-client:dev/data' 对应的 value
     */
    @Value("${config.info}")
    private String configInfo;

    /**
     * http://localhost:9101/configInfo
     *
     * 从 Consul 配置中心中获取配置信息
     */
    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return configInfo;
    }
}
