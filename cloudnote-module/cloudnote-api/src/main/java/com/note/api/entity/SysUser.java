package com.note.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

/**
 * 用户表
 *
 * @date 2022/11/29 22:03
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    private String email;
    private String nickname;
    private String username;
    private String password;
    private Boolean status;
    private Set<SysRole> roles;
}
