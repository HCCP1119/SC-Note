package com.note.tsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证授权，服务调用中心
 *
 * @date 2022/11/22 15:55
 **/

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class NoteTscApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoteTscApplication.class, args);
    }
}
