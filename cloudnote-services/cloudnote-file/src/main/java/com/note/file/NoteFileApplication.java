package com.note.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 文件管理中心
 *
 * @date 2022/12/28 15:54
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class NoteFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoteFileApplication.class, args);
    }
}
