package com.note.umc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户管理中心
 *
 * @date 2022/11/22 15:55
 **/

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class NoteUmcApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoteUmcApplication.class, args);
    }
}
