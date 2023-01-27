package com.note.umc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 登录日志实体类
 *
 * @date 2023/1/25 20:54
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_login_logs")
public class UserLoginLogs {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 请求令牌
     */
    private String token;
    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date loginTime;
    /**
     * 设备
     */
    private String device;
    /**
     * 系统
     */
    @TableField("`system`")
    private String system;
    /**
     * 浏览器
     */
    private String browser;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 地址
     */
    private String address;
    /**
     * 状态
     */
    private Integer status;
    /**
     * userId
     */
    private Long uid;

}
