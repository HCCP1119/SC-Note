package com.note.tsc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.note.api.entity.SysUser;
import com.note.api.result.R;

import com.note.tsc.entity.RegisterBody;
import com.note.tsc.entity.SysUserView;
import com.note.tsc.feign.CodeMailService;
import com.note.tsc.mapper.RoleMapper;
import com.note.tsc.mapper.UserInfoMapper;
import com.note.tsc.mapper.UserMapper;
import com.note.web.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户服务
 *
 * @date 2022/11/22 15:59
 **/
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final CodeMailService codeMailService;
    private final UserMapper userMapper;
    private final UserInfoMapper userInfoMapper;
    private final RoleMapper roleMapper;
    private final RedisUtils redisServer;

    @PostMapping("/register/{email}")
    @PermitAll
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

    @PostMapping("/register")
    @PermitAll
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
                this.passwordEncoder.encode(user.getPassword())
        );
        userMapper.insert(sysUser);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.select("id").eq("username",user.getUsername());
        roleMapper.registerUserRole(userMapper.selectOne(wrapper).getId());
        return R.ok("注册成功");
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public R<?> testVer(){
        return R.fail("success");
    }

    @GetMapping("/test1")
    @PermitAll
    public  R<?> testVer1(){
        return R.fail("success");
    }

    @PostMapping("/getUser")
    @PreAuthorize("hasRole('USER')")
    public R<?> getUser(@RequestParam("uid") Long id){
        SysUser user = userMapper.findById(id);
        user.setPassword(null);
        SysUserView userinfo = userInfoMapper.selectById(id);
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);
        map.put("userinfo",userinfo);
        return R.ok(map,"success");
    }

    @PostMapping("/saveInfo")
    @PreAuthorize("hasRole('USER')")
    public R<?> saveInfo(@RequestParam("uid") Long id,@RequestParam("nickname") String nickname,@RequestParam("introduce") String introduce){
        userMapper.setNickname(nickname,id);
        userInfoMapper.setIntroduce(introduce,id);
        System.out.println(id);
        System.out.println(nickname);
        System.out.println(introduce);
        return getUser(id);
    }
}
