package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

@EnableOAuth2Sso // 启用Oauth2单点登录
@SpringBootApplication
public class Oauth2ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ClientApplication.class, args);
    }

    /**
     * 在浏览器上操作：
     *
     * 访问客户端需要授权的接口
     * GET http://localhost:9501/user/getCurrentUser
     * 需要登录，跳转到登录页面
     * GET http://localhost:9501/login
     * 继续跳转到认证服务器
     * GET http://localhost:9401/oauth/authorize?client_id=admin&redirect_uri=http://localhost:9501/login&response_type=code&state=5eQR3K
     * 跳转到认证服务器登录页面
     * GET http://localhost:9401/login
     *
     * 用户输入账号密码，点击登录（后续不需要重复输入）
     * POST http://localhost:9401/login
     * GET http://localhost:9401/oauth/authorize?client_id=admin&redirect_uri=http://localhost:9501/login&response_type=code&state=5eQR3K
     * 用户点击同意授权（可以设为静默）
     * POST http://localhost:9401/oauth/authorize
     *
     * 生成授权码，跳转回客户端
     * GET http://localhost:9501/login?code=YqpD3p&state=ha8Lae
     *
     * 客户端带着授权码，后台请求令牌
     * POST http://localhost:9401/oauth/token
     * 客户端请求令牌成功，接口正常响应
     * GET http://localhost:9501/user/getCurrentUser
     */

}
