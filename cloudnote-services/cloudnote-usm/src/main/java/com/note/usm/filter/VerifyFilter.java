package com.note.usm.filter;

import com.note.api.constant.TokenConstants;
import com.note.web.utils.JWTUtils;
import com.note.web.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import org.json.JSONException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

/**
 * 验证认证过滤器
 *
 * @date 2022/11/29 21:12
 **/
public class VerifyFilter extends BasicAuthenticationFilter {

    private final RedisUtils redisServer;

    public VerifyFilter(AuthenticationManager authenticationManager, RedisUtils redisServer) {
        super(authenticationManager);
        this.redisServer = redisServer;
    }


    public void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String header = request.getHeader(TokenConstants.AUTHENTICATION);
        if (header == null || !header.startsWith(TokenConstants.PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        //请求头获取token并去掉前缀
        String token = header.replace(TokenConstants.PREFIX, "");
        try {
            Claims claims = JWTUtils.parToken(token);
            String username = claims.getSubject();
            List<String> roles = redisServer.getList(username + RedisUtils.ROLE_SUF);
            List<String> permissions = redisServer.getList(username + RedisUtils.PERMISSION_SUF);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    AuthorityUtils.createAuthorityList(
                            Stream
                                    .concat(roles.stream().map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r), permissions.stream())
                                    .toArray(String[]::new)
                    ));
            //权限写入上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.doFilterInternal(request, response, chain);
    }
}

