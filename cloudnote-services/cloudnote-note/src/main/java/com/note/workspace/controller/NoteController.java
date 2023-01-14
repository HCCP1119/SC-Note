package com.note.workspace.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.note.api.result.R;
import com.note.workspace.entity.Note;
import com.note.workspace.entity.SearchCondition;
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
    private final FolderMapper folderMapper;
    private final WorkspaceMapper workspaceMapper;
    private final NoteMapper noteMapper;


    @PostMapping("/addNote")
    public R<?> addNote(@RequestBody Folder folder){
        String id = IdUtil.simpleUUID();
        folder.setId(id);
        Note note = new Note(id,"",folder.getLabel(),folder.getParentId(),folder.getUid());
        noteMapper.insert(note);
        folderMapper.insert(folder);
        return R.ok("添加成功");
    }

    @PostMapping("/saveTitle/{id}/{title}")
    public R<?> saveTitle(@PathVariable("id") String id,@PathVariable("title") String title){
        UpdateWrapper<Note> noteWrapper = new UpdateWrapper<>();
        UpdateWrapper<Folder> folderWrapper = new UpdateWrapper<>();
        noteWrapper.eq("id",id);
        noteWrapper.set("title",title);
        folderWrapper.eq("id",id);
        folderWrapper.set("label",title);
        noteMapper.update(null,noteWrapper);
        folderMapper.update(null,folderWrapper);
        return R.ok("success");
    }

    @PostMapping("/saveNote")
    public R<?> saveNote(@RequestBody Note note){
        noteMapper.updateNote(note);
        UpdateWrapper<Folder> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",note.getId());
        wrapper.set("label",note.getTitle());
        folderMapper.update(null,wrapper);
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
}
