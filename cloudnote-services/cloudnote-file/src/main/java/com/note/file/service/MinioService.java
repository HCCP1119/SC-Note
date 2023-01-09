package com.note.file.service;

import cn.hutool.core.util.IdUtil;
import com.note.file.Properties.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
        String fileName = IdUtil.simpleUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            PutObjectArgs args = PutObjectArgs
                    .builder()
                    .bucket(properties.getBucket())
                    .object(fileName)
                    .stream(file.getInputStream(), file.getInputStream().available(), -1)
                    .contentType(file.getContentType())
                    .build();
            client.putObject(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://192.168.1.128:9000/" + properties.getBucket() + "/" + fileName;
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
            e.printStackTrace();
        }
    }
}
