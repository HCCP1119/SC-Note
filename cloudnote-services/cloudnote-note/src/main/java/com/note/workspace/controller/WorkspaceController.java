package com.note.workspace.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.note.api.result.R;
import com.note.web.utils.SaTokenUtils;
import com.note.workspace.entity.Note;
import com.note.workspace.entity.Workspace;
import com.note.workspace.mapper.NoteMapper;
import com.note.workspace.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 目录控制器
 *
 * @date 2023/1/14 15:20
 **/
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/note")
public class WorkspaceController {

    private final WorkspaceMapper workspaceMapper;
    private final NoteMapper noteMapper;

    @GetMapping("/getTree")
    @SaCheckPermission("NOTE")
    public R<?> getTree(){
        Long userId = SaTokenUtils.getLoginUserId();
        List<Workspace> tree = workspaceMapper.getTree(userId);
        return R.ok(tree,"获取成功");
    }

    @GetMapping("/getTree/{id}")
    @SaCheckPermission("NOTE")
    public R<?> getTree(@PathVariable("id") String id){
        Workspace workspace = workspaceMapper.getById(id);
        Workspace folder = workspaceMapper.getById(id);
        List<Workspace> tree = workspaceMapper.getChild(id);
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
    @SaCheckPermission("NOTE")
    public R<?> addWorkspace(@RequestBody Workspace workspace){
        String id = IdUtil.simpleUUID();
        workspace.setId(id);
        workspaceMapper.insert(workspace);
        return R.ok(id,"添加成功");
    }

    @PostMapping("/rename")
    @SaCheckPermission("NOTE")
    public R<?> rename(@RequestBody Workspace workspace){
        UpdateWrapper<Workspace> workspaceWrapper = new UpdateWrapper<>();
        workspaceWrapper.eq("id",workspace.getId());
        workspaceWrapper.set("label",workspace.getLabel());
        workspaceMapper.update(null,workspaceWrapper);
        if (workspace.getType().equals("note")){
            UpdateWrapper<Note> noteWrapper = new UpdateWrapper<>();
            noteWrapper.eq("id",workspace.getId());
            noteWrapper.set("title",workspace.getLabel());
            noteMapper.update(null,noteWrapper);
        }
        return R.ok(workspace.getId(),"修改成功");
    }

    @DeleteMapping("/remove/{id}")
    @SaCheckPermission("NOTE")
    public R<?> remove(@PathVariable("id") String id){
        List<String> folderList = new ArrayList<>();
        List<String> notes = new ArrayList<>();
        selectChildListById(id,folderList,notes);
        folderList.add(id);
        notes.add(id);
        workspaceMapper.deleteById(id);
        noteMapper.deleteById(id);
        folderList.forEach(workspaceMapper::fakeDel);
        notes.forEach(noteMapper::fakeDel);
        return R.ok("success");
    }


    @GetMapping("/removeList")
    @SaCheckPermission("NOTE")
    public R<?> getRemoveList(){
        Long userId = SaTokenUtils.getLoginUserId();
        List<Workspace> removeList = workspaceMapper.getRemoveList(userId);
        return R.ok(removeList,"success");
    }

    @PostMapping("/restore/{id}")
    @SaCheckPermission("NOTE")
    public R<?> restore(@PathVariable("id") String id){
        List<String> folderList = new ArrayList<>();
        List<String> notes = new ArrayList<>();
        selectChildListById(id,folderList,notes);
        folderList.add(id);
        notes.add(id);
        folderList.forEach(workspaceMapper::restore);
        notes.forEach(noteMapper::restore);
        return R.ok("success");
    }

    @DeleteMapping("/delete/{id}")
    @SaCheckPermission("NOTE")
    public R<?> delete(@PathVariable("id") String id){
        List<String> folderList = new ArrayList<>();
        List<String> noteList = new ArrayList<>();
        selectChildListById(id,folderList,noteList);
        noteList.add(id);
        folderList.add(id);
        folderList.forEach(workspaceMapper::delete);
        noteList.forEach(noteMapper::delete);
        return R.ok("success");
    }
    private void selectChildListById(String id, List<String> folderList, List<String> noteList){
        List<Workspace> childList  = workspaceMapper.getChild(id);
        childList.forEach(folder -> {
            folderList.add(folder.getId());
            if (folder.getType().equals("note")){
                noteList.add(folder.getId());
            }
            selectChildListById(folder.getId(),folderList,noteList);
        });
    }

    @GetMapping("/shareList")
    @SaCheckPermission("NOTE")
    public R<?> getShareList(){
        Long userId = SaTokenUtils.getLoginUserId();
        List<Workspace> shareList = workspaceMapper.getShareList(userId);
        return R.ok(shareList,"success");
    }

    @PostMapping("/share/{method}/{id}")
    @SaCheckPermission("NOTE")
    public R<?> share(@PathVariable("id") String id,@PathVariable("method") String method){
        UpdateWrapper<Workspace> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id);
        if (method.equals("shared")){
            wrapper.set("share",1);
        }else{
            wrapper.set("share",0);
        }
        workspaceMapper.update(null,wrapper);
        return R.ok("success");
    }
}
