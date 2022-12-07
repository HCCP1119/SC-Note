package com.note.mail.controller;

import com.note.mail.entity.CodeMail;
import com.note.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Todo
 *
 * @date 2022/12/5 21:25
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    @PostMapping("/registerCode/{email}/{ip}")
    public Boolean sendCode(@PathVariable("email") final String email, @PathVariable("ip") final String ip){
        CodeMail codeMail = new CodeMail(email, ip);
        return mailService.sendCode(codeMail);
    }
}
