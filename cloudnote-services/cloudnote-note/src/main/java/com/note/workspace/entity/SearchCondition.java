package com.note.workspace.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 前端传入的搜索条件
 *
 * @date 2023/1/10 17:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCondition {
    /**
     * input值
     */
    private String value;
    /**
     * 搜索类型
     */
    private String searchType;
    /**
     * 排序类型
     */
    private String sortType;
    /**
     * 创建时间（开始）
     */
    private Date createStart;
    /**
     * 创建时间（结束）
     */
    private Date createEnd;

    /**
     * 最后修改时间（开始）
     */
    private Date updateStart;
    /**
     * 最后修改时间（结束）
     */
    private Date updateEnd;
}
