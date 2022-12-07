package com.note.mail.service;

import cn.hutool.core.util.RandomUtil;
import com.note.mail.entity.CodeMail;
import com.note.web.utils.RedisUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

/**
 * 邮件服务
 *
 * @date 2022/11/23 14:57
 **/
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final RedisUtils redisServer;

    @Value("${spring.mail.username}")
    private String from;

    public Boolean sendCode(final CodeMail mail){
        //随机生成一个四位数的验证码
        String  code = RandomUtil.randomString(6);
        //使用模板
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("title","邮箱验证码");
        String content = templateEngine.process("mail",context);
        //创建简单邮件消息
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        //发送邮件账户
        try {
            helper.setFrom(from);
            //谁要接收
            helper.setTo(mail.getEmail());
            //邮件标题
            helper.setSubject("验证你的注册邮箱");
            //邮件内容
            helper.setText(content,true);
            javaMailSender.send(message);

            // 判断该ip发送验证码的次数并进行限制
            if(!(redisServer.hasKey(mail.getIp()+"_COUNT"))){
                redisServer.setObject(mail.getIp()+"_COUNT",1,1L,TimeUnit.HOURS);
            }
            redisServer.incrByKey(mail.getIp()+"_COUNT");
            //缓存验证码
            redisServer.setObject(mail.getEmail()+RedisUtils.MAILCODE_SUF,code,10L, TimeUnit.MINUTES);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}
