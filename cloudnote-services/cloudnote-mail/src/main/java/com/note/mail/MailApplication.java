package com.note.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 邮件管理中心
 *
 * @date 2022/11/22 22:59
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class MailApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class, args);
    }
}
