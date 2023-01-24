package com.note.umc.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.note.api.entity.SysUser;
import com.note.api.result.R;
import com.note.umc.entity.LoginBody;
import com.note.umc.entity.RegisterBody;
import com.note.umc.feign.CodeMailService;
import com.note.umc.mapper.RoleMapper;
import com.note.umc.mapper.UserMapper;
import com.note.web.utils.RedisUtils;
import com.note.web.utils.SaTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证控制器
 *
 * @date 2023/1/23 15:59
 **/
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final RedisUtils redisServer;
    private final CodeMailService codeMailService;

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody loginUser){
        SysUser user = userMapper.findByUsernameOrEmail(loginUser.getUsername(), loginUser.getUsername());
        if (user==null){
            return R.fail("用户名不存在");
        }
        if (BCrypt.checkpw(loginUser.getPassword(),user.getPassword())){
            StpUtil.login(user.getId());
            SaTokenUtils.setLoginUser(user);
            String token = StpUtil.getTokenValue();
            SaHolder.getResponse().addHeader("Authorization",token);
            SaHolder.getResponse().addHeader("Access-Control-Expose-Headers","Authorization");
            return R.ok(user.getId(),"登录成功");
        }
        return R.fail("用户名或密码错误");
    }

    @PostMapping("/register/{email}")
    public R<?> sendRegisterCode(@PathVariable("email") final String email, final HttpServletRequest request){
        if (redisServer.hasKey(request.getRemoteAddr()+"_COUNT")){
            Integer count = redisServer.getObject( request.getRemoteAddr()+"_COUNT");
            if (count > 5){
                return R.fail("发送频繁，请稍后再试");
            }
        }
        Boolean isSend = codeMailService.sendCode(email, request.getRemoteAddr());
        if (isSend){
            return R.ok("发送成功");
        }
        return R.fail("发送失败，请稍后再试");
    }

    @PostMapping("register")
    public R<?> apiRegister(@RequestBody final RegisterBody user) {
        String verifyCode = redisServer.getObject(user.getEmail() + RedisUtils.MAILCODE_SUF);
        if (user.getAuthCode().isEmpty() || !user.getAuthCode().equals(verifyCode)){
            return R.fail("无效验证码");
        }
        if (userMapper.hasEmail(user.getEmail(),user.getUsername())){
            return R.fail("该邮箱或用户名已注册");
        }
        final SysUser sysUser = new SysUser(
                user.getEmail(),
                user.getNickname(),
                user.getUsername(),
                BCrypt.hashpw(user.getPassword())
        );
        userMapper.insert(sysUser);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.select("id").eq("username",user.getUsername());
        roleMapper.registerUserRole(userMapper.selectOne(wrapper).getId());
        return R.ok("注册成功");
    }

}
