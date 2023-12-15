package com.macro.cloud.component;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.macro.cloud.domain.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限验证接口扩展
 *
 * 因为每个项目的需求不同，其权限设计也千变万化，因此 [获取当前账号权限码集合] 这一操作不可能内置到框架中，
 * 所以 Sa-Token 将此操作以接口的方式暴露给你，以方便你根据自己的业务逻辑进行重写。
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限码列表
        UserDTO userDTO = (UserDTO) StpUtil.getSession().get("userInfo");
        return userDTO.getPermissionList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色码列表
        return null;
    }

}

