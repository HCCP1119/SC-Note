package com.note.web.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.StpUtil;
import com.note.api.entity.SysUser;
import lombok.NoArgsConstructor;

/**
 * 存储和获取登录用户信息
 *
 * @date 2023/1/23 15:52
 **/
@NoArgsConstructor
public class SaTokenUtils {

    private static final String LOGIN_USER_KEY = "loginUser";

    /**
     * 设置登录用户数据
     *
     * @param user 用户
     */
    public static void setLoginUser(SysUser user){
        SaHolder.getStorage().set(LOGIN_USER_KEY,user);
        StpUtil.getTokenSession().set(LOGIN_USER_KEY,user);
    }

    /**
     * 获取登录用户
     *
     * @return {@link SysUser}
     */
    public static SysUser getLoginUser(){
        SysUser loginUser = (SysUser) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        loginUser = (SysUser) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        return loginUser;
    }

    public static Long getLoginUserId(){
        return StpUtil.getLoginIdAsLong();
    }
}
