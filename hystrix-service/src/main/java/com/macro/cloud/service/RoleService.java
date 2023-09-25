package com.macro.cloud.service;

import com.macro.cloud.domain.CommonResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Sumkor
 * @since 2023/9/25
 */
@Service
public class RoleService {

    private Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    @HystrixCommand()
    public CommonResult getRole(Long id) {
        LOGGER.info("getRole id:{}, thread:{}", id, Thread.currentThread().getName());
        return new CommonResult<>(id);
    }

    @HystrixCommand(threadPoolKey = "PoolAAA")
    public CommonResult getRoleCommand(Long id) {
        LOGGER.info("getRoleCommand id:{}, thread:{}", id, Thread.currentThread().getName());
        return new CommonResult<>(id);
    }
}
