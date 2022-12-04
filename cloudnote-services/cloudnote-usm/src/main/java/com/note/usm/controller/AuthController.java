package com.note.usm.controller;

import com.note.api.entity.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;


/**
 * 验证控制器
 *
 * @date 2022/11/22 15:59
 **/
@RestController
@CrossOrigin
public class AuthController {


    @PostMapping("/register")
    public String apiRegister(@RequestBody final SysUser user) {

        return "ok";
    }

    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('admin')")
    public String testVer(){
        return "success";
    }
}
