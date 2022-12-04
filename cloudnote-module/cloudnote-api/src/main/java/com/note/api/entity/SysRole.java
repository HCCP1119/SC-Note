package com.note.api.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

/**
 * 角色表
 *
 * @date 2022/11/29 22:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role")
public class SysRole {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    private String description;
    private Boolean status;
    private Set<String> permissions;
}
