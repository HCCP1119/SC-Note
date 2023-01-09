package com.note.file.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.note.api.result.R;
import com.note.file.entity.File;
import com.note.file.mapper.FileMapper;
import com.note.file.mapper.UserInfoMapper;
import com.note.file.service.MinioService;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 文件控制器
 *
 * @date 2022/12/28 17:00
 **/
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final MinioService service;
    private final UserInfoMapper userInfoMapper;
    private final FileMapper fileMapper;

    @PostMapping("/noteImage")
    public R<?> uploadNoteImg(@RequestParam("img") MultipartFile img){
        String path = service.upload(img);
        return R.ok(path,"success");
    }

    @PostMapping("/headImage")
    public R<?> uploadHeadImg(@RequestParam("img") MultipartFile img,@RequestParam("uid") Long id){
        String disk = userInfoMapper.getImgDisk(id);
        if (disk!=null){
            service.removeFile(disk);
        }
        String path = service.upload(img);
        String fileName = path.substring(path.lastIndexOf("/")+1);
        userInfoMapper.setHeadImg(path,fileName,id);
        return R.ok(path,"success");
    }

    @PostMapping("/uploadFile")
    public R<?> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("uid") Long id){
        String disk = service.upload(file);
        String contentType = null;
        if (Objects.requireNonNull(file.getContentType()).startsWith("application")){
            contentType = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
        }else {
            contentType = file.getContentType();
        }
        File dataFile = new File(disk,file.getOriginalFilename(),file.getSize(),id,contentType);
        fileMapper.insert(dataFile);
        return getFiles(id);
    }

    @GetMapping("/getFiles")
    public R<?> getFiles(@RequestParam("uid") Long id){
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",id);
        List<File> files = fileMapper.selectList(wrapper);
        return R.ok(files,"success");
    }

    @GetMapping("/getFiles/{basis}/{sequence}/{id}")
    public R<?> getByOrder(@PathVariable("basis") String basis,@PathVariable("sequence") String sequence,@PathVariable("id") Long id){
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",id);
        if (sequence.equals("ASC")){
            wrapper.orderByAsc(basis);
        }else{
            wrapper.orderByDesc(basis);
        }
        List<File> files = fileMapper.selectList(wrapper);
        return R.ok(files,"success");
    }

}
