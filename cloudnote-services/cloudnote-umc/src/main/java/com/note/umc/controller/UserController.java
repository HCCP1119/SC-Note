package com.note.umc.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.TokenSign;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.note.api.entity.SysUser;
import com.note.api.result.R;

import com.note.umc.entity.ResetPasswordBody;
import com.note.umc.entity.SysUserView;
import com.note.umc.entity.UserLoginLogs;
import com.note.umc.mapper.LoginLogMapper;
import com.note.umc.mapper.UserInfoMapper;
import com.note.umc.mapper.UserMapper;
import com.note.web.utils.SaTokenUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 用户服务
 *
 * @date 2022/11/22 15:59
 **/
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserMapper userMapper;
    private final UserInfoMapper userInfoMapper;
    private final LoginLogMapper loginLogMapper;

    @GetMapping("/test")
    @SaCheckPermission("ME")
    public R<?> testVer(){
        Object user = StpUtil.getTokenSession().get("loginUser");
        System.out.println(user);
        return R.ok("success");
    }

    @GetMapping("/test1")
    public  R<?> testVer1(){
        String header = SaHolder.getRequest().getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        String type = userAgent.getBrowser().getName();
        String system = userAgent.getOperatingSystem().getName();
        System.out.println(type);
        System.out.println(system);
        return R.ok("success");
    }

    @GetMapping("/getUser")
    @SaCheckPermission("ME")
    public R<?> getUser(){
        Long id = SaTokenUtils.getLoginUserId();
        SysUser user = userMapper.findById(id);
        user.setPassword(null);
        SysUserView userinfo = userInfoMapper.selectById(id);
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);
        map.put("userinfo",userinfo);
        return R.ok(map,"success");
    }

    @PostMapping("/saveInfo")
    @SaCheckPermission("ME")
    public R<?> saveInfo(@RequestParam("nickname") String nickname,@RequestParam("introduce") String introduce){
        Long id = SaTokenUtils.getLoginUserId();
        userMapper.setNickname(nickname,id);
        userInfoMapper.setIntroduce(introduce,id);
        return getUser();
    }


    @GetMapping("/token")
    @SaCheckLogin
    public R<?> tokenList(){
        Long userId = SaTokenUtils.getLoginUserId();
        QueryWrapper<UserLoginLogs> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",userId).orderByDesc("status");
        List<UserLoginLogs> loginLogs = loginLogMapper.selectList(wrapper);
        return R.ok(loginLogs,"success");
    }

    @DeleteMapping("/kickout")
    @SaCheckPermission("ME")
    public R<?> kickout(@RequestBody String token){
        String tokenValue = StpUtil.searchTokenValue(token,0,-1,true).get(0);
        StpUtil.logoutByTokenValue(tokenValue.split("satoken:login:token:")[1]);
        return tokenList();
    }

    @DeleteMapping("/delToken/{logId}")
    @SaCheckPermission("ME")
    public R<?> deletedToken(@PathVariable("logId") Long logId){
        loginLogMapper.deleteById(logId);
        return tokenList();
    }

    @PostMapping("/resetPassword")
    @SaCheckPermission("ME")
    public R<?> resetPassword(@RequestBody ResetPasswordBody password){
        Long userId = SaTokenUtils.getLoginUserId();
        if(BCrypt.checkpw(password.getOldPassword(),userMapper.findById(userId).getPassword())){
            userMapper.resetPassword(BCrypt.hashpw(password.getNewPassword()),userId);
            StpUtil.logout();
            return R.ok("修改成功,请重新登录");
        }
        return R.fail("密码错误");
    }
}
