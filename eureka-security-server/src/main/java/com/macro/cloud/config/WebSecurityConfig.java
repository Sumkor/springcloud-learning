package com.macro.cloud.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * CSRF（Cross-Site Request Forgery）是一种攻击方法，利用用户在已登录的Web应用程序上执行非本意的操作，与XSS不同，它利用的是网站对用户浏览器的信任.
 *
 * 可以简单的理解为：攻击者可以盗用你的登陆信息，以你的身份模拟发送各种请求。
 * 对服务器来说这个请求是完全合法的，但是却完成了攻击者所期望的一个操作。
 * 比如以你的名义发送邮件、发消息，盗取你的账号，添加系统管理员，甚至于购买商品、虚拟货币转账等。
 *
 * Spring Security中的CSRF保护功能通过生成和验证CSRF令牌来防止此类攻击。
 * CSRF令牌是具有唯一性且与会话关联的令牌，在用户进行重要操作（例如提交表单）时会被嵌入到请求中。
 * 当服务器收到请求时，它会验证请求中的CSRF令牌是否与用户会话中的令牌匹配，以确认请求的合法性。
 *
 * Created by macro on 2019/8/28.
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 默认情况下添加SpringSecurity依赖的应用每个请求都需要添加CSRF token才能访问，
     * Eureka客户端注册时并不会添加，所以需要配置/eureka/**路径不需要CSRF token。
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/eureka/**");
        super.configure(http);
    }
}
