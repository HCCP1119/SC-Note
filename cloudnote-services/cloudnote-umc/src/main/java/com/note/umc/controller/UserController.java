package com.note.umc.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.note.api.entity.SysUser;
import com.note.api.result.R;

import com.note.umc.entity.SysUserView;
import com.note.umc.mapper.UserInfoMapper;
import com.note.umc.mapper.UserMapper;
import com.note.web.utils.SaTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/users")
public class UserController {

    private final UserMapper userMapper;
    private final UserInfoMapper userInfoMapper;

    @GetMapping("/test")
    @SaCheckPermission("ME")
    public R<?> testVer(){
        Object user = StpUtil.getTokenSession().get("loginUser");
        System.out.println(user);
        return R.fail("success");
    }

    @GetMapping("/test1")
    public  R<?> testVer1(){
        return R.fail("success");
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
}
