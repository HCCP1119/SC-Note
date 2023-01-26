package com.note.umc.listener;

import cn.dev33.satoken.listener.SaTokenListenerForSimple;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.note.umc.entity.UserLoginLogs;
import com.note.umc.mapper.LoginLogMapper;
import com.note.web.utils.SaTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sa-Token全局监听器
 *
 * @date 2023/1/26 16:15
 **/
@Component
@RequiredArgsConstructor
public class UserListener extends SaTokenListenerForSimple {

    private final LoginLogMapper logMapper;

    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        Long userId = (Long) loginId;
        List<String> tokenValueListByLoginId = StpUtil.getTokenValueListByLoginId(userId);
        UpdateWrapper<UserLoginLogs> wrapper = new UpdateWrapper<>();
        wrapper.eq("uid",userId);
        for (String token : tokenValueListByLoginId) {
            wrapper.eq("token",token.substring(token.lastIndexOf(".")+1)).set("status",1);
        }
    }

    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        String token = tokenValue.substring(tokenValue.lastIndexOf(".") + 1);
        UpdateWrapper<UserLoginLogs> wrapper = new UpdateWrapper<>();
        wrapper.eq("token",token).set("status",0);
        logMapper.update(null,wrapper);
    }

    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        String token = tokenValue.substring(tokenValue.lastIndexOf(".") + 1);
        UpdateWrapper<UserLoginLogs> wrapper = new UpdateWrapper<>();
        wrapper.eq("token",token).set("status",0);
        logMapper.update(null,wrapper);
    }
}
