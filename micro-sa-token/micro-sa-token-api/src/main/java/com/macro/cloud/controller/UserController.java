package com.macro.cloud.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.macro.cloud.api.CommonResult;
import com.macro.cloud.domain.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取登录用户信息接口
 * Created by macro on 2020/6/19.
 */
@RestController
@RequestMapping("/user")
public class UserController{

    /**
     * 由于使用了 Redis 实现分布式 Session，直接从 Session 中获取用户信息即可
     */
    @GetMapping("/info")
    public CommonResult<UserDTO> userInfo() {
        UserDTO userDTO = (UserDTO) StpUtil.getSession().get("userInfo");
        return CommonResult.success(userDTO);
    }

}
