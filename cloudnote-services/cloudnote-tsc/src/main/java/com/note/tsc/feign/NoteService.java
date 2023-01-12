package com.note.tsc.feign;


import com.note.api.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "cloudnote-note",path = "/note")
public interface NoteService {

    @GetMapping("/getTree")
    R<?> getTree();

    @GetMapping("/getTree/{id}")
    R<?> getTree(@PathVariable("id") String id);

    @PostMapping("/addWorkspace")
    R<?> addWorkspace(@RequestBody Object workspace);

    @PostMapping("/addFolder")
    R<?> addFolder(@RequestBody Object folder);

    @PostMapping("/rename/workspace")
    R<?> renameWorkspace(@RequestBody Object workspace);

    @PostMapping("/rename/folder")
    R<?> renameFolder(@RequestBody Object folder);

    @PostMapping("/addNote")
    R<?> addNote(@RequestBody Object folder);

    @PostMapping("/saveNote")
    R<?> saveNote(@RequestBody Object note);

    @GetMapping("/getNote/{id}")
    R<?> getNote(@PathVariable("id") String id);

    @PostMapping("/search")
    R<?> search(@RequestBody Object condition);
}
