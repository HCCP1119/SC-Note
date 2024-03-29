package com.note.file.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.note.api.constant.HttpStatus;
import com.note.api.result.R;
import com.note.file.entity.File;
import com.note.file.entity.RemoveImg;
import com.note.file.mapper.FileMapper;
import com.note.file.mapper.UserInfoMapper;
import com.note.file.service.MinioService;
import com.note.web.utils.SaTokenUtils;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import okhttp3.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Collections;
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
    @SaCheckPermission("FILE")
    public R<?> uploadHeadImg(@RequestParam("img") MultipartFile img){
        Long userId = SaTokenUtils.getLoginUserId();
        String disk = userInfoMapper.getImgDisk(userId);
        if (disk!=null){
            service.removeFile(disk);
        }
        String path = service.upload(img);
        String fileName = path.substring(path.lastIndexOf("/")+1);
        userInfoMapper.setHeadImg(path,fileName,userId);
        return R.ok(path,"success");
    }

    @PostMapping("/uploadFile")
    @SaCheckPermission("FILE")
    public R<?> uploadFile(@RequestParam("file") MultipartFile file){
        Long userId = SaTokenUtils.getLoginUserId();
        String disk = service.upload(file);
        String contentType = null;
        if (Objects.requireNonNull(file.getContentType()).startsWith("application")){
            contentType = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
        }else {
            contentType = file.getContentType();
        }
        File dataFile = new File(disk,file.getOriginalFilename(),file.getSize(),userId,contentType);
        fileMapper.insert(dataFile);
        return getFiles();
    }

    @GetMapping("/getFiles")
    @SaCheckPermission("FILE")
    public R<?> getFiles(){
        Long userId = SaTokenUtils.getLoginUserId();
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<File> files = fileMapper.selectList(wrapper);
        return R.ok(files,"success");
    }

    @GetMapping("/getFiles/{basis}/{sequence}")
    @SaCheckPermission("FILE")
    public R<?> getByOrder(@PathVariable("basis") String basis,@PathVariable("sequence") String sequence){
        Long userId = SaTokenUtils.getLoginUserId();
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        if (sequence.equals("ASC")){
            wrapper.orderByAsc(basis);
        }else{
            wrapper.orderByDesc(basis);
        }
        List<File> files = fileMapper.selectList(wrapper);
        return R.ok(files,"success");
    }

    @GetMapping("/download")
    public ResponseEntity<?> download(@RequestParam("disk") String disk){
        Long userId = SaTokenUtils.getLoginUserId();
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("disk",disk).and(i -> i.eq("user_id",userId));
        File file = fileMapper.selectOne(wrapper);
        if (Objects.isNull(file)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(R.fail(HttpStatus.NOT_FOUND,"指定文件不存在"));
        }
        String path = disk.substring(disk.lastIndexOf("/")+1);
        try {
            final GetObjectResponse response = service.download(path);
            String[] type = Objects.requireNonNull(response.headers().get("content-type")).split("/");
            final HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment;filename=\"%s\"", path));
            headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.setContentType(new MediaType(type[0],type[1]));
            headers.setContentLength(file.getSize());
            return ResponseEntity.ok().headers(headers).body(new InputStreamResource(response));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FAIL).body(R.fail(HttpStatus.FAIL,"获取文件异常"));
        }
    }

    @DeleteMapping("/remove")
    @SaCheckPermission("FILE")
    public R<?> remove(@RequestParam("disk") String disk){
        Long userId = SaTokenUtils.getLoginUserId();
        String path = disk.substring(disk.lastIndexOf("/")+1);
        try {
            service.removeFile(path);
            QueryWrapper<File> wrapper = new QueryWrapper<>();
            wrapper.eq("disk",disk).and(i -> i.eq("user_id",userId));
            fileMapper.delete(wrapper);
            return getFiles();
        }catch (Exception e){
            return R.fail("服务器异常");
        }
    }

    @DeleteMapping("/removeNoteImg")
    public R<?> remove(@RequestBody RemoveImg removeImg){
        List<String> removeImgList = removeImg.getRemoveImgList();
        removeImgList.forEach(i -> {
            String path = i.substring(i.lastIndexOf("/") + 1);
            service.removeFile(path);
        });
        return R.ok("success");
    }
}
