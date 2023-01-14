package com.note.workspace.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 笔记实体
 *
 * @date 2022/12/29 16:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    private String id;
    private String content;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    private String title;
    @TableLogic
    private Integer deleted;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    private String parentId;
    private Long uid;

    public Note(String id, String content, String title, String parentId,Long uid) {
        this.id = id;
        this.content = content;
        this.title = title;
        this.parentId = parentId;
        this.uid = uid;
    }
}
