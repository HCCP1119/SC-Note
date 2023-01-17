package com.note.workspace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.workspace.entity.Workspace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


import java.util.List;

@Mapper
public interface WorkspaceMapper extends BaseMapper<Workspace> {
    List<Workspace> getTree(Long id);

    List<Workspace> getChild(String id);

    @Select("select * from workspace where id=#{id}")
    Workspace getById(String id);

    @Select("select id,create_time,update_time,label,type,isEdit,icon,parent_id,uid from workspace where uid=#{id} and deleted = 1")
    List<Workspace> getRemoveList(Long id);

    @Update("update workspace set deleted=0 where id=#{id}")
    void restore(String id);

    @Delete("delete from workspace where id=#{id}")
    void delete(String id);

    @Select("select id,create_time,update_time,label,type,isEdit,icon,parent_id,uid from workspace where uid=#{id} and share=1 and deleted=0")
    List<Workspace> getShareList(Long id);
}