package com.note.tsc.feign;


import com.note.api.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(value = "cloudnote-note",path = "/note")
public interface NoteService {

    @GetMapping("/getTree")
    R<?> getTree(@RequestParam("uid") Long id);

    @GetMapping("/getTree/{id}")
    R<?> getTree(@PathVariable("id") String id);

    @PostMapping("/addWorkspace")
    R<?> addWorkspace(@RequestBody Object workspace);

    @PostMapping("/addFolder")
    R<?> addFolder(@RequestBody Object folder);

    @PostMapping("/rename")
    R<?> renameWorkspace(@RequestBody Object workspace);

    @PostMapping("/addNote")
    R<?> addNote(@RequestBody Object folder);

    @PostMapping("/saveNote")
    R<?> saveNote(@RequestBody Object note);

    @GetMapping("/getNote/{id}")
    R<?> getNote(@PathVariable("id") String id);

    @PostMapping("/search")
    R<?> search(@RequestBody Object condition);

    @PostMapping("/saveTitle/{id}/{title}")
    R<?> saveTitle(@PathVariable("id") String id,@PathVariable("title") String title);

    @DeleteMapping("/remove/{id}")
    R<?> remove(@PathVariable("id") String id);
}
