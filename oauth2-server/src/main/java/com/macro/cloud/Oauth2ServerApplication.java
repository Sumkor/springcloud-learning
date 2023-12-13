package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.ui.LoginPageGeneratingWebFilter;

@SpringBootApplication
public class Oauth2ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication.class, args);
    }

    /**
     * OAuth 主要应用在授权第三方用户。
     * 数据的所有者（用户）告诉系统，同意授权第三方应用进入系统，获取这些数据。
     * 系统从而产生一个短期的进入令牌（token），用来代替用户密码，供第三方应用使用。
     *
     * OAuth2 相关名词解释
     *
     * Resource owner（资源拥有者）（用户）：拥有该资源的最终用户，他有访问资源的账号密码；
     * Resource server（资源服务器）：拥有受保护资源的服务器，如果请求包含正确的访问令牌，可以访问资源；
     * Client（客户端）（第三方应用）：访问资源的客户端，会使用访问令牌去获取资源服务器的资源，可以是浏览器、移动设备或者服务器；
     * Authorization server（认证服务器）：用于认证用户的服务器，如果客户端（第三方应用）认证通过，发放访问资源服务器的令牌。
     *
     * 注：本例中，当前应用 Oauth2ServerApplication 同时作为资源服务器和认证服务器，第三方应用则为浏览器、postman工具。
     */

    /**
     * 四种授权模式
     *
     * Authorization Code（授权码模式）：
     * 正宗的 OAuth2 的授权模式，客户端先将用户导向认证服务器，登录后获取授权码，客户端根据授权码获取访问令牌，后续即可带着访问令牌请求资源服务器。
     * 这种方式是最常用的流程，安全性也最高，它适用于那些有后端的 Web 应用。
     * 授权码通过前端传送（URL明文传递），令牌则是储存在后端，而且所有与资源服务器的通信都在后端完成。这样的前后端分离，可以避免令牌泄漏。
     * 在颁发授权码的时候，获得了用户与授权码之间的映射关系，所以在用授权码申请令牌的时候，就可以具体到用户了。
     *
     * Implicit（简化模式、隐藏模式）：
     * 和授权码模式相比，取消了获取授权码的过程，用户授权通过后，直接获取访问令牌。
     * 适用于纯前端应用，没有后端。令牌只能储存在前端，因此允许直接向前端颁发令牌。
     *
     * Resource Owner Password Credentials（密码模式）：
     * 客户端直接向用户获取用户名和密码，之后向认证服务器获取访问令牌。
     * 如果用户高度信任某个应用，这种模式允许用户把用户名和密码直接告诉该应用。该应用就使用用户名和密码申请令牌。
     *
     * Client Credentials（客户端模式）：
     * 客户端直接通过客户端认证（比如 client_id 和 client_secret）从认证服务器获取访问令牌。
     * 适用于没有前端的命令行应用，即在命令行下请求令牌。
     * 这种方式给出的令牌，是针对第三方应用的，而不是针对用户的，即有可能多个用户共享同一个令牌。
     *
     * 注意，不管哪一种授权方式，第三方应用申请令牌之前，都必须先到系统备案，说明自己的身份，然后会拿到两个身份识别码：客户端 ID（client ID）和客户端密钥（client secret）。
     * 这是为了防止令牌被滥用，没有备案过的第三方应用，是不会拿到令牌的。
     */

    /**
     * 授权码模式使用
     *
     * 在浏览器访问进行登录授权
     * GET http://localhost:9401/oauth/authorize?response_type=code&client_id=admin&redirect_uri=http://www.baidu.com&scope=all&state=normal
     * @see AuthorizationEndpoint#authorize(java.util.Map, java.util.Map, org.springframework.web.bind.support.SessionStatus, java.security.Principal)
     *
     * 跳转到登录页面
     * GET http://localhost:9401/login
     * @see LoginPageGeneratingWebFilter#createPage(org.springframework.web.server.ServerWebExchange, java.lang.String)
     *
     * 输入账号密码进行登录操作
     * POST http://localhost:9401/login
     * @see UsernamePasswordAuthenticationFilter#attemptAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     *
     * 登录后进行授权操作
     * POST http://localhost:9401/oauth/authorize
     * @see AuthorizationEndpoint#approveOrDeny(java.util.Map, java.util.Map, org.springframework.web.bind.support.SessionStatus, java.security.Principal)
     *
     * 之后会浏览器会带着授权码跳转到我们指定的路径
     * GET https://www.baidu.com/?code=eTsADY&state=normal
     *
     * 使用授权码请求访问令牌（使用 client_id 和 client_secret 构造一个 Authorization 头信息）
     * POST http://localhost:9401/oauth/token
     * @see TokenEndpoint#postAccessToken(java.security.Principal, java.util.Map)
     *
     * 在请求头中添加访问令牌，访问需要登录认证的接口进行测试，发现已经可以成功访问
     * GET http://localhost:9401/user/getCurrentUser
     */
}
