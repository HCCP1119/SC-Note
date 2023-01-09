package com.note.tsc.feign;

import com.note.api.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Component
@FeignClient(value = "cloudnote-file",path = "/file")
public interface FileService {

    @PostMapping("/noteImage")
    R<?> uploadNoteImg(@RequestParam("img") MultipartFile img);

    @PostMapping("/headImage")
    R<?> uploadHeadImg(@RequestParam("img") MultipartFile img,@RequestParam("uid") Long id);
}
