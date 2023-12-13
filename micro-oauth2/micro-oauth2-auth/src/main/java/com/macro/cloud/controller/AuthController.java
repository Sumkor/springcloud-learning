package com.macro.cloud.controller;

import com.macro.cloud.api.CommonResult;
import com.macro.cloud.domain.Oauth2TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.config.xml.AuthorizationServerBeanDefinitionParser;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.DispatcherServlet;

import java.security.Principal;
import java.util.Map;

/**
 * 自定义Oauth2获取令牌接口
 * Created by macro on 2020/7/17.
 */
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    /**
     * Oauth2登录认证
     * 请求头必须带 Authorization，否则报错 401 Unauthorized
     */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public CommonResult<Oauth2TokenDto> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead("Bearer ").build();
        return CommonResult.success(oauth2TokenDto);
        /**
         * 有个疑问 /oauth/token 为什么不会跟 TokenEndpoint#postAccessToken 方法冲突报 Ambiguous mapping
         *
         * 打断点调试
         * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.MappingRegistry#validateMethodMapping
         *
         * 可知 TokenEndpoint 中的 /oauth/token，对应的 beanName=oauth2EndpointHandlerMapping，beanClass=FrameworkEndpointHandlerMapping
         * 当前 AuthController 中的 /oauth/token，对应的 beanName=requestMappingHandlerMapping，beanClass=RequestMappingHandlerMapping
         *
         * 只有在同一个 bean 的情况下，才会报 Ambiguous mapping！
         *
         * 配置 TokenEndpoint 的位置
         * @see AuthorizationServerBeanDefinitionParser
         * @see AuthorizationServerSecurityConfiguration#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
         *
         * 最终，请求过来的时候，进入 DispatcherServlet，从这里遍历的顺序，RequestMappingHandlerMapping 优先级比 FrameworkEndpointHandlerMapping 高！
         * @see DispatcherServlet#getHandler(javax.servlet.http.HttpServletRequest)
         */
    }

    @RequestMapping(value = "/token1", method = RequestMethod.POST)
    public CommonResult<Oauth2TokenDto> postAccessToken1() {
        return CommonResult.success(null, "测试1");
        /**
         * 请求报错403，定位到抛出异常位置，无法获取token
         *
         * @see FilterChainProxy.VirtualFilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
         * @see FilterSecurityInterceptor#invoke(org.springframework.security.web.FilterInvocation)
         *
         * 解决：
         * 基于 spring security，为了防止跨站提交攻击，通常会启用 csrf，所有 http 请求都被会 CsrfFilter 拦截，
         * 而 CsrfFilter 中有一个私有类 DefaultRequiresCsrfMatcher，POST 方法被排除在外了，也就是说只有 GET|HEAD|TRACE|OPTIONS 这 4 类方法会被放行。
         * https://blog.csdn.net/qq_41299347/article/details/121161821
         *
         * @see CsrfFilter.DefaultRequiresCsrfMatcher
         */
    }

//    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public CommonResult<Oauth2TokenDto> postAccessToken() {
        return CommonResult.success(null, "测试");
        /**
         * WebSecurityConfig 中配置了
         *
         * http.csrf().disable()
         * .authorizeRequests()
         * .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
         * .antMatchers("/oauth/**").permitAll()
         * .anyRequest().authenticated();
         *
         * 此时，请求还是会报错 401
         *
         * 一个可能的原因，在源码中配置的 WebSecurityConfig 覆盖了项目配置！
         * @see AuthorizationServerSecurityConfiguration#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
         *
         * 获取白名单配置关键代码
         * @see AbstractSecurityInterceptor#beforeInvocation(java.lang.Object)
         *
         * Collection<ConfigAttribute> attributes = this.obtainSecurityMetadataSource().getAttributes(object);
         *
         * 可知，/oauth/token 地址对应的配置是 fullyAuthenticated，因此需要校验权限！！
         */
    }


}
