package com.macro.cloud;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Sumkor
 * @since 2023/10/25
 */
public class EncodeTest {

    @Test
    public void clientPwd() {
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        String secret = b.encode("admin123456");
        System.out.println(secret);
    }

    @Test
    public void baseAuth() {
        String auth = "admin" + ":" + "admin123456";
        String base64Auth = Base64.encodeBase64URLSafeString(auth.getBytes());
        System.out.println("Basic " + base64Auth);
    }
}
