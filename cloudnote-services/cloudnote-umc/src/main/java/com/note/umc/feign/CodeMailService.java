package com.note.umc.feign;


import com.note.umc.filter.FeignInterceptor;
import com.note.web.handler.GlobalSentinelHandler;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(value = "cloudnote-mail",path = "/mail",configuration = FeignInterceptor.class)
public interface CodeMailService {
    @PostMapping("/registerCode/{email}/{ip}")
    Boolean sendCode(@PathVariable("email") final String email, @PathVariable("ip") final String ip);
}
