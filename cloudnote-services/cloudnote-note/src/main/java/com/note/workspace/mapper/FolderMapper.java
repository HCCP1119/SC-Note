package com.note.workspace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.workspace.entity.Folder;
import com.note.workspace.entity.Note;
import com.note.workspace.entity.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FolderMapper extends BaseMapper<Folder> {
    List<Folder> getTree(String pid);

    void rename(String label,String id);

    @Select("select * from folder where id=#{id}")
    Folder getById(String id);
}