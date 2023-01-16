package com.note.workspace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.workspace.entity.Note;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;



@Mapper
public interface NoteMapper extends BaseMapper<Note> {

    @Update("update note set content=#{content},title=#{title} where id=#{id}")
    void updateNote(Note note);

    @Update("update note set deleted=0 where id=#{id}")
    void restore(String id);

    @Delete("delete from note where id=#{id}")
    void delete(String id);

}
