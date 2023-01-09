package com.note.workspace.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 文件夹
 *
 * @date 2022/12/23 14:55
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("folder")
public class Folder{
    private String id;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    private String label;
    private String type;
    @TableField("isEdit")
    private Boolean isEdit;
    private String icon;
    private List<Folder> children;
    private String parentId;
}