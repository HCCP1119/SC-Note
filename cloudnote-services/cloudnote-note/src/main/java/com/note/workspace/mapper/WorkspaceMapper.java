package com.note.workspace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.workspace.entity.Workspace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WorkspaceMapper extends BaseMapper<Workspace> {
    List<Workspace> getTree(Long id);

    List<Workspace> getChild(String id);

    void rename(String label,String id);

    @Select("select * from workspace where id=#{id}")
    Workspace getById(String id);
}