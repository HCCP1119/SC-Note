package com.note.workspace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.workspace.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;



@Mapper
public interface NoteMapper extends BaseMapper<Note> {

    @Update("update note set content=#{content},title=#{title} where id=#{id}")
    void updateNote(Note note);


}
