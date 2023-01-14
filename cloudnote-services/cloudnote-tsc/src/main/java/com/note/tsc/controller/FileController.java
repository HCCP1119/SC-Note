package com.note.tsc.controller;

import com.note.api.result.R;
import com.note.tsc.feign.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *调用文件服务
 *
 * @date 2023/1/3 22:12
 **/
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @PostMapping("/noteImage")
    @PreAuthorize("hasRole('USER')")
    public R<?> uploadNoteImg(@RequestParam("img") MultipartFile img){
       return fileService.uploadNoteImg(img);
    }

    @PostMapping("/headImage")
    @PreAuthorize("hasRole('USER')")
    public R<?> uploadHeadImg(@RequestParam("img") MultipartFile img,@RequestParam("uid") Long id){
        return fileService.uploadHeadImg(img,id);
    }
}
