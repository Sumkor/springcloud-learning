package com.macro.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sumkor
 * @since 2023/9/21
 */
@RestController
@RequestMapping("/discovery")
public class DiscoveryClientController {

    @Value("${spring.application.name}")
    private String serviceId;

    @Autowired
    private DiscoveryClient discoveryClient;// Eureka客户端，可以获取到服务实例信息

    /**
     * http://localhost:8101/discovery/get
     */
    @GetMapping("/get")
    public Map<String, Integer> get() {
        Map<String, Integer> map = new HashMap<>();
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        for (ServiceInstance instance : instances) {
            System.out.println("http://" + instance.getHost() + ":" + instance.getPort());
            map.put(instance.getHost(), instance.getPort());
        }
        return map;
    }
}
