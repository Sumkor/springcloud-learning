package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Oauth2JwtServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2JwtServerApplication.class, args);
    }

    /**
     * JWT 是 JSON WEB TOKEN 的缩写，它是基于 RFC 7519 标准定义的一种可以安全传输的的 JSON 对象，由于使用了数字签名，所以是可信任和安全的。
     *
     * JWT token的格式：header.payload.signature；
     *   header 中用于存放签名的生成算法；
     *   payload 中用于存放数据，比如过期时间、用户名、用户所拥有的权限等；
     *   signature 为以 header 和 payload 生成的签名，一旦 header 和 payload 被篡改，验证将失败。
     */

}
