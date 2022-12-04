package com.note.usm.config;

import com.note.usm.filter.VerifyFilter;
import com.note.usm.handler.AuthenticationHandler;
import com.note.usm.mapper.UserMapper;
import com.note.usm.security.UserDetailManager;
import com.note.web.config.DefaultSecurityConfig.SecurityConfigAdapter;
import com.note.web.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Spring Security 配置
 *
 * @date 2022/11/24 17:43
 **/
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends SecurityConfigAdapter {

    private final UserMapper userMapper;
    private final RedisUtils redisServer;
    private final AuthenticationHandler authenticationHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .anyRequest().permitAll();
        http.formLogin()
                //验证成功处理器
                .successHandler(this.authenticationHandler)
                //验证失败处理器
                .failureHandler(this.authenticationHandler)
                .and()
                //添加验证认证过滤器
                .addFilter(new VerifyFilter(authenticationManagerBean(), redisServer));
        //注销成功处理器
        http.logout().logoutSuccessHandler(this.authenticationHandler);
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailManager(this.userMapper,this.redisServer);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
