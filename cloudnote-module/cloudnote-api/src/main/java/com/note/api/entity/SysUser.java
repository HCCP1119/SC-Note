package com.note.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
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
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 邮件
     */
    private String email;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 状态
     */
    private Boolean status;
    /**
     * 角色
     */
    private Set<SysRole> roles;

    public SysUser(String email, String nickname, String username, String password) {
        this.email = email;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }
}
