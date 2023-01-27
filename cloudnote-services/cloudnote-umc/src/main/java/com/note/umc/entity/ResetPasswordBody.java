package com.note.umc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 重置密码实体
 *
 * @date 2023/1/26 21:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordBody {
    private String oldPassword;
    private String newPassword;
    private String email;
    private String authCode;
}
