package com.note.tsc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 *
 * @date 2023/1/2 19:33
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_info")
public class SysUserView {
    private Long id;
    @TableField("headImage")
    private String headImage;
    private String introduce;
}
