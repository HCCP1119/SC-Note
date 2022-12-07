package com.note.mail.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码实体
 *
 * @date 2022/11/22 23:01
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeMail {
    private String email;
    private String ip;
}
