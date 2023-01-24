package com.note.umc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录实体
 *
 * @date 2023/1/23 16:12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginBody {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
