package com.note.umc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册实体
 *
 * @date 2022/12/6 15:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterBody {

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 邮件
     */
    private String email;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String authCode;
}
