package com.note.file.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 文件实体
 *
 * @date 2023/1/7 14:20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("file")
public class File {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String disk;
    private String name;
    private Long size;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private Date uploadTime;
    private Long userId;
    private String contentType;

    public File(String disk, String name, Long size, Long userId, String contentType) {
        this.disk = disk;
        this.name = name;
        this.size = size;
        this.userId = userId;
        this.contentType = contentType;
    }

}
