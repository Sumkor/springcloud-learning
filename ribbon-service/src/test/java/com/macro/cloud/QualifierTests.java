package com.macro.cloud;

import com.macro.cloud.annotation.MyQualifier;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sumkor
 * @since 2023/9/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QualifierTests {

    @Autowired
    @MyQualifier
    private RestTemplate aRestTemplate;

    @Autowired
    @LoadBalanced
    private RestTemplate bRestTemplate;

    @Test
    public void getBean() {
        Assert.assertNotEquals(aRestTemplate, bRestTemplate);
    }
}
