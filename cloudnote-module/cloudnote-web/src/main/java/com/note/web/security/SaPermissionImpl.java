package com.note.web.security;

import cn.dev33.satoken.stp.StpInterface;
import com.note.api.entity.SysRole;
import com.note.api.entity.SysUser;
import com.note.web.utils.SaTokenUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 获取角色列表和权限列表
 *
 * @date 2023/1/23 15:52
 **/
@Component
public class SaPermissionImpl implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        SysUser user = SaTokenUtils.getLoginUser();
        Set<SysRole> roles = user.getRoles();
        return roles.stream().flatMap(sysRole -> sysRole.getPermissions().stream()).distinct().collect(Collectors.toList());
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        SysUser user = SaTokenUtils.getLoginUser();
        Set<SysRole> roles = user.getRoles();
        return roles.stream().map(SysRole::getName).collect(Collectors.toList());
    }
}
