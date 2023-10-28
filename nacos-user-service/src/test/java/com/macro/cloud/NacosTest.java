package com.macro.cloud;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;
import org.junit.Test;

import java.util.List;

/**
 * @author Sumkor
 * @since 2023/10/28
 */
public class NacosTest {

    private static final String address = "127.0.0.1:8848";

    @Test
    public void publishConfig() throws Exception {
        ConfigService configService = NacosFactory.createConfigService(address);
        configService.publishConfig("dataId", "group", "123");
    }

    @Test
    public void getConfig() throws NacosException {
        ConfigService configService = NacosFactory.createConfigService(address);
        String config = configService.getConfig("dataId", "group", 1000);
        System.out.println("config = " + config);

        boolean b = configService.removeConfig("dataId", "group");
        System.out.println("b = " + b);
    }

    @Test
    public void registerInstance() throws Exception {
        NamingService namingService = NacosFactory.createNamingService(address);
        namingService.registerInstance("serviceA", "172.20.3.5", 8082);

        Thread.sleep(10000000);
    }

    @Test
    public void maintain() throws Exception {
        NamingService namingService = NacosFactory.createNamingService(address);

        List<Instance> allInstances = namingService.getAllInstances("serviceA");
        Instance instance = allInstances.get(0);
        instance.addMetadata("pwd", "123456");

        NamingMaintainService maintainService = NacosFactory.createMaintainService(address);
        maintainService.updateInstance("serviceA", instance);

        Service serviceA = maintainService.queryService("serviceA");
        System.out.println("serviceA = " + serviceA);
    }

    @Test
    public void queryService() throws Exception {
        NamingMaintainService maintainService = NacosFactory.createMaintainService(address);
        Service serviceA = maintainService.queryService("serviceA");
        System.out.println("serviceA = " + serviceA);
    }
}
