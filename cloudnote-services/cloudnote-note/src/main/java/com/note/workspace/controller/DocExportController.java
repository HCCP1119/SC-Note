package com.note.workspace.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.note.workspace.entity.Note;
import com.note.workspace.mapper.NoteMapper;
import com.note.workspace.util.FileExport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 前端控制器
 *
 * @date 2023/4/1 17:22
 **/
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/note")
public class DocExportController {
    private final NoteMapper noteMapper;

    @PostMapping("/2md/{id}")
    public ResponseEntity<byte[]> html2md(@PathVariable("id") String id){
        QueryWrapper<Note> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        Note note = noteMapper.selectOne(wrapper);
        ResponseEntity<byte[]> responseEntity = FileExport.html2md(note.getTitle(),note.getContent());
        return responseEntity;
    }
}
