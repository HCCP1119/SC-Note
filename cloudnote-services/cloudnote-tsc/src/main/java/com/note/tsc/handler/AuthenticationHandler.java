package com.note.tsc.handler;

import com.note.api.constant.HttpStatus;
import com.note.api.constant.TokenConstants;
import com.note.api.entity.SysUser;
import com.note.api.result.R;
import com.note.api.utils.RUtils;
import com.note.web.details.UserDetails;
import com.note.web.utils.JWTUtils;
import com.note.web.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义验证处理类
 *
 * @date 2022/11/30 0:07
 **/
@Component
@RequiredArgsConstructor
public class AuthenticationHandler implements
        AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler {

    private final RedisUtils redisServer;

    //验证失败
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        RUtils.toResponse(R.fail(HttpStatus.UNAUTHORIZED, exception.getMessage()), response);
    }

    //验证成功
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        final UserDetails details = (UserDetails) authentication.getPrincipal();
        String token = JWTUtils.createToken(details.getUsername().toUpperCase(), details.getUserId());
        response.addHeader(TokenConstants.AUTHENTICATION, TokenConstants.PREFIX + token);
        response.addHeader("Access-Control-Expose-Headers", TokenConstants.AUTHENTICATION);
        RUtils.toResponse(R.ok(details.getUserId(), "登录成功"), response);
    }

    //注销成功
    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        final UserDetails details = (UserDetails) authentication.getPrincipal();
        redisServer.deleteObject(details.getUsername() + RedisUtils.ROLE_SUF);
        redisServer.deleteObject(details.getUsername() + RedisUtils.PERMISSION_SUF);
        RUtils.toResponse(R.fail(HttpStatus.OK, "注销成功"), response);
    }
}
