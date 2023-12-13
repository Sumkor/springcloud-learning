package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Oauth2GatewayApplication {

    /**
     * 使用密码模式获取JWT令牌，访问地址：http://localhost:9201/auth/oauth/token
     * 使用获取到的JWT令牌访问需要权限的接口，访问地址：http://localhost:9201/api/hello
     * 使用获取到的JWT令牌访问获取当前登录用户信息的接口，访问地址：http://localhost:9201/api/user/currentUser
     */
    public static void main(String[] args) {
        SpringApplication.run(Oauth2GatewayApplication.class, args);
    }

}
