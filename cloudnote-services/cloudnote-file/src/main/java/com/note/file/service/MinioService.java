package com.note.file.service;

import cn.hutool.core.util.IdUtil;
import com.note.file.Properties.MinioProperties;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


/**
 * MinioService
 *
 * @date 2022/12/29 13:54
 **/
@RequiredArgsConstructor
@Service
public class MinioService {

    private final MinioClient client;
    private final MinioProperties properties;

    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileName = IdUtil.simpleUUID() + Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        try {
            PutObjectArgs args = PutObjectArgs
                    .builder()
                    .bucket(properties.getBucket())
                    .object(fileName)
                    .stream(file.getInputStream(), file.getInputStream().available(), -1)
                    .contentType(file.getContentType())
                    .build();
            client.putObject(args);
            return "http://192.168.1.128:9000/" + properties.getBucket() + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("上传失败");
        }
    }

    public GetObjectResponse download(String path){
        try {
            GetObjectArgs args =GetObjectArgs.builder().bucket(properties.getBucket()).object(path).build();
            return client.getObject(args);
        } catch (Exception e) {
            throw new RuntimeException("获取文件失败");
        }
    }

    public void removeFile(String disk){
        try {
            RemoveObjectArgs args = RemoveObjectArgs
                    .builder()
                    .bucket(properties.getBucket())
                    .object(disk)
                    .build();
            client.removeObject(args);
        } catch (Exception e) {
            throw new RuntimeException("删除失败");
        }
    }
}
