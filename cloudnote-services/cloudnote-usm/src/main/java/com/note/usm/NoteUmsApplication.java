package com.note.usm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户管理中心
 *
 * @date 2022/11/22 15:55
 **/

@SpringBootApplication
@EnableDiscoveryClient
public class NoteUmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoteUmsApplication.class, args);
    }
}
