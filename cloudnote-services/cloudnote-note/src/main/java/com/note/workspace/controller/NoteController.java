package com.note.workspace.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.note.api.result.R;
import com.note.workspace.entity.Note;
import com.note.workspace.entity.SearchCondition;
import com.note.workspace.entity.Workspace;
import com.note.workspace.mapper.NoteMapper;
import com.note.workspace.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Objects;

/**
 * 笔记控制器
 *
 * @date 2022/12/11 21:32
 **/
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {
    private final WorkspaceMapper workspaceMapper;
    private final NoteMapper noteMapper;


    @PostMapping("/addNote")
    public R<?> addNote(@RequestBody Workspace folder){
        String id = IdUtil.simpleUUID();
        folder.setId(id);
        Note note = new Note(id,"",folder.getLabel(),folder.getParentId(),folder.getUid());
        noteMapper.insert(note);
        workspaceMapper.insert(folder);
        return R.ok(id,"添加成功");
    }

    @PostMapping("/saveTitle/{id}/{title}")
    public R<?> saveTitle(@PathVariable("id") String id,@PathVariable("title") String title){
        UpdateWrapper<Note> noteWrapper = new UpdateWrapper<>();
        UpdateWrapper<Workspace> folderWrapper = new UpdateWrapper<>();
        noteWrapper.eq("id",id);
        noteWrapper.set("title",title);
        folderWrapper.eq("id",id);
        folderWrapper.set("label",title);
        noteMapper.update(null,noteWrapper);
        workspaceMapper.update(null,folderWrapper);
        return R.ok("success");
    }

    @PostMapping("/saveNote")
    public R<?> saveNote(@RequestBody Note note){
        noteMapper.updateNote(note);
        return R.ok("保存成功");
    }

    @GetMapping("/getNote/{id}")
    public R<?> getNote(@PathVariable("id") String id){
        Note note = noteMapper.selectById(id);
        if (Objects.isNull(note.getContent())){
            note.setContent("");
        }
        return R.ok(note,"success");
    }

    @PostMapping("/search")
    public R<?> search(@RequestBody SearchCondition condition){
        QueryWrapper<Note> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",condition.getUserId());
        if (condition.getValue()!=null){
            wrapper.like("title",condition.getValue());
        }
        if (condition.getCreateStart()!=null){
            wrapper.between("create_time",condition.getCreateStart(),condition.getCreateEnd());
        }
        if (condition.getUpdateStart()!=null){
            wrapper.between("update_time",condition.getUpdateStart(),condition.getUpdateEnd());
        }
        if (condition.getSortType().equals("ASC")){
            wrapper.orderByAsc("title");
        }
        if (condition.getSortType().equals("DESC")){
            wrapper.orderByDesc("title");
        }
        List<Note> notes = noteMapper.selectList(wrapper);
        return R.ok(notes,"success");
    }

    @GetMapping("/starNote")
    public R<?> starNote(@RequestParam("id") Long id){
        QueryWrapper<Note> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",id);
        wrapper.eq("star",1);
        wrapper.orderByDesc("update_time");
        List<Note> notes = noteMapper.selectList(wrapper);
        return R.ok(notes,"success");
    }

    @PostMapping("/star/{method}/{id}")
    public R<?> star(@PathVariable("id") String id,@PathVariable("method") String method){
        UpdateWrapper<Note> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id);
        if (method.equals("stared")){
            wrapper.set("star",1);
        }else{
            wrapper.set("star",0);
        }
        noteMapper.update(null,wrapper);
        return R.ok("success");
    }

}
