package com.macro.cloud.config;

import com.macro.cloud.component.JwtTokenEnhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 使用 JWT 存储 token 的配置
 * Created by macro on 2019/10/8.
 */
@Configuration
public class JwtTokenStoreConfig {

    /**
     * 向 TokenEndpoint 请求 AccessToken 的时候，需要从 tokenStore 获取或存储 AccessToken
     * @see TokenEndpoint#postAccessToken(java.security.Principal, java.util.Map)
     * @see AbstractTokenGranter#grant(java.lang.String, org.springframework.security.oauth2.provider.TokenRequest)
     * @see DefaultTokenServices#createAccessToken(org.springframework.security.oauth2.provider.OAuth2Authentication)
     *
     * 可知 JwtTokenStore 并不会存储 token（因为令牌本身存有用户信息）
     * @see JwtTokenStore#getAccessToken(org.springframework.security.oauth2.provider.OAuth2Authentication)
     * @see JwtTokenStore#storeAccessToken(org.springframework.security.oauth2.common.OAuth2AccessToken, org.springframework.security.oauth2.provider.OAuth2Authentication)
     */
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 生成 JWT 令牌的位置
     * @see JwtAccessTokenConverter#enhance(org.springframework.security.oauth2.common.OAuth2AccessToken, org.springframework.security.oauth2.provider.OAuth2Authentication)
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("test_key");//配置JWT使用的秘钥
        return accessTokenConverter;
    }

    /**
     * 扩展 JWT 中存储的内容
     */
    @Bean
    public JwtTokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }
}
