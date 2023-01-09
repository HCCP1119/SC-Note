package com.note.workspace.controller;




import cn.hutool.core.util.IdUtil;
import com.note.api.result.R;
import com.note.workspace.entity.Folder;
import com.note.workspace.entity.Note;
import com.note.workspace.entity.Workspace;
import com.note.workspace.mapper.FolderMapper;
import com.note.workspace.mapper.NoteMapper;
import com.note.workspace.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/getTree")
    public R<?> getTree(){
        List<Workspace> tree = workspaceMapper.getTree();
        return R.ok(tree,"获取成功");
    }

    @GetMapping("/getTree/{id}")
    public R<?> getTree(@PathVariable("id") String id){
        Workspace workspace = workspaceMapper.getById(id);
        Folder folder = folderMapper.getById(id);
        List<Folder> tree = folderMapper.getTree(id);
        Map<String,Object> map = new HashMap<>();
        map.put("tree",tree);
        if (workspace==null){
            map.put("tab",folder);
        }else {
            map.put("tab",workspace);
        }
        return R.ok(map,"获取成功");
    }

    @PostMapping("/addWorkspace")
    public R<?> addWorkspace(@RequestBody Workspace workspace){
        String id = IdUtil.simpleUUID();
        workspace.setId(id);
        workspaceMapper.insert(workspace);
        List<Workspace> tree = workspaceMapper.getTree();
        return R.ok(tree,"添加成功");
    }

    @PostMapping("/addFolder")
    public R<?> addFolder(@RequestBody Folder folder){
        String id = IdUtil.simpleUUID();
        folder.setId(id);
        folderMapper.insert(folder);
        System.out.println(folder);
        List<Workspace> tree = workspaceMapper.getTree();
        return R.ok(tree,"添加成功");
    }

    @PostMapping("/rename/workspace")
    public R<?> rename(@RequestBody Workspace workspace){
        workspaceMapper.rename(workspace.getLabel(),workspace.getId());
        List<Workspace> tree = workspaceMapper.getTree();
        return R.ok(tree,"修改成功");
    }

    @PostMapping("/rename/folder")
    public R<?> rename(@RequestBody Folder folder){
        folderMapper.rename(folder.getLabel(),folder.getId());
        List<Workspace> tree = workspaceMapper.getTree();
        return R.ok(tree,"修改成功");
    }

    @PostMapping("/addNote")
    public R<?> addNote(@RequestBody Folder folder){
        String id = IdUtil.simpleUUID();
        folder.setId(id);
        Note note = new Note(id,null,folder.getLabel(),folder.getParentId());
        noteMapper.insert(note);
        folderMapper.insert(folder);
        System.out.println(folder);
        List<Workspace> tree = workspaceMapper.getTree();
        return R.ok(tree,"添加成功");
    }

    @PostMapping("/saveNote")
    public R<?> saveNote(@RequestBody Note note){
        noteMapper.updateNote(note);
        return R.ok("保存成功");
    }

    @GetMapping("/getNote/{id}")
    public R<?> getNote(@PathVariable("id") String id){
        Note note = noteMapper.selectById(id);
        return R.ok(note,"success");
    }
}
