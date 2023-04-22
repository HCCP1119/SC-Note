package com.note.workspace.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 工作区
 *
 * @date 2022/12/23 14:55
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("workspace")
public class Workspace {
    private String id;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    private String label;
    private String type;
    @TableField("isEdit")
    private Boolean isEdit;
    @TableLogic
    private Integer deleted;
    private Integer share;
    private String icon;
    private Long uid;
    private String parentId;
    @TableField("status")
    private Integer status;
    private List<Workspace> children;
}