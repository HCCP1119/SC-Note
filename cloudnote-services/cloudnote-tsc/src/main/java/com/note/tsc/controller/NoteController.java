package com.note.tsc.controller;

import com.alibaba.csp.sentinel.util.IdUtil;
import com.note.api.result.R;
import com.note.tsc.feign.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 调用笔记服务
 *
 * @date 2022/12/24 15:19
 **/
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/getTree")
    @PreAuthorize("hasRole('USER')")
    public R<?> getTree(@RequestParam("uid") Long id){
       return noteService.getTree(id);
    }

    @GetMapping("/getTree/{id}")
    public R<?> getTree(@PathVariable("id") String id){
        return noteService.getTree(id);
    }

    @PostMapping("/addWorkspace")
    @PreAuthorize("hasRole('USER')")
    public R<?> addWorkspace(@RequestBody Object workspace){
        return noteService.addWorkspace(workspace);
    }

    @PostMapping("/addFolder")
    @PreAuthorize("hasRole('USER')")
    public R<?> addFolder(@RequestBody Object folder){
        return noteService.addFolder(folder);
    }

    @PostMapping("/rename")
    @PreAuthorize("hasRole('USER')")
    public R<?> renameWorkspace(@RequestBody Object workspace){
        return noteService.renameWorkspace(workspace);
    }

    @PostMapping("/addNote")
    @PreAuthorize("hasRole('USER')")
    public R<?> addNote(@RequestBody Object folder){
        return noteService.addNote(folder);
    }

    @PostMapping("/saveNote")
    @PreAuthorize("hasRole('USER')")
    public R<?> saveNote(@RequestBody Object note){
        return noteService.saveNote(note);
    }

    @GetMapping("/getNote/{id}")
    @PreAuthorize("hasRole('USER')")
    public R<?> getNote(@PathVariable("id") String id){
        return noteService.getNote(id);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public R<?> search(@RequestBody Object condition){
        return noteService.search(condition);
    }

    @PostMapping("/saveTitle/{id}/{title}")
    @PreAuthorize("hasRole('USER')")
    public R<?> saveTitle(@PathVariable("id") String id,@PathVariable("title") String title){
        return noteService.saveTitle(id,title);
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasRole('USER')")
    public R<?> remove(@PathVariable("id") String id){
        return noteService.remove(id);
    }

    @GetMapping("/removeList")
    @PreAuthorize("hasRole('USER')")
    public R<?> getRemoveList(@RequestParam("uid") Long id){
        return noteService.getRemoveList(id);
    }

    @GetMapping("/starNote")
    @PreAuthorize("hasRole('USER')")
    R<?> starNote(@RequestParam("id") Long id){
        return noteService.starNote(id);
    }

    @GetMapping("/shareList")
    @PreAuthorize("hasRole('USER')")
    public R<?> getShareList(@RequestParam("uid") Long id){
        return noteService.getShareList(id);
    }
}
