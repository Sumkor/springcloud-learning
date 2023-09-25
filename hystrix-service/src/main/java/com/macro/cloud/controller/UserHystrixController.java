package com.macro.cloud.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.macro.cloud.domain.CommonResult;
import com.macro.cloud.domain.User;
import com.macro.cloud.service.RoleService;
import com.macro.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by macro on 2019/9/3.
 */
@RestController
@RequestMapping("/user")
public class UserHystrixController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * http://localhost:8401/user/testFallback/1
     */
    @GetMapping("/testFallback/{id}")
    public CommonResult testFallback(@PathVariable Long id) {
        return userService.getUser(id);
    }

    /**
     * http://localhost:8401/user/testCommand/1
     */
    @GetMapping("/testCommand/{id}")
    public CommonResult testCommand(@PathVariable Long id) {
        return userService.getUserCommand(id);
    }

    @GetMapping("/testException/{id}")
    public CommonResult testException(@PathVariable Long id) {
        return userService.getUserException(id);
    }

    /**
     * http://localhost:8401/user/testCache/1
     */
    @GetMapping("/testCache/{id}")
    public CommonResult testCache(@PathVariable Long id) {
        userService.getUserCache(id);
        userService.getUserCache(id);
        userService.getUserCache(id);
        return new CommonResult("操作成功", 200);
    }

    /**
     * http://localhost:8401/user/testRemoveCache/1
     */
    @GetMapping("/testRemoveCache/{id}")
    public CommonResult testRemoveCache(@PathVariable Long id) {
        userService.getUserCache(id);
        userService.removeCache(id);
        userService.getUserCache(id);
        return new CommonResult("操作成功", 200);
    }

    /**
     * http://localhost:8401/user/testCollapser
     */
    @GetMapping("/testCollapser")
    public CommonResult testCollapser() throws ExecutionException, InterruptedException {
        Future<User> future0 = userService.getUserFuture(1L);
        Future<User> future1 = userService.getUserFuture(1L);
        Future<User> future2 = userService.getUserFuture(2L);
        future0.get();
        future1.get();
        future2.get();
        ThreadUtil.safeSleep(200);
        Future<User> future3 = userService.getUserFuture(3L);
        future3.get();
        return new CommonResult("操作成功", 200);
    }

    /**
     * http://localhost:8401/user/testPool/1
     *
     * 结论：
     * 1. HystrixCommand 注解中的 groupKey、threadPoolKey 会指定特定的线程池
     * 2. HystrixCommand 注解没有指定线程池的情况下，会根据注入的 Service 自动划分线程池
     */
    @GetMapping("/testPool/{id}")
    public CommonResult testPool(@PathVariable Long id) {
        userService.getUser(id);
        roleService.getRole(id);

        userService.getUserCommand(id);
        roleService.getRoleCommand(id);
        return new CommonResult<>("操作成功", 200);
    }
}
