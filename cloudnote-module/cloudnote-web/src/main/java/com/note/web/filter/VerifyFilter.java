package com.note.web.filter;

import com.note.api.constant.HttpStatus;
import com.note.api.constant.TokenConstants;
import com.note.api.result.R;
import com.note.api.utils.RUtils;
import com.note.web.utils.JWTUtils;
import com.note.web.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
@RequiredArgsConstructor
@Component
public class VerifyFilter extends OncePerRequestFilter {

    private final RedisUtils redisServer;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
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
            List<String> roles = redisServer.getList(username.toUpperCase() + RedisUtils.ROLE_SUF);
            List<String> permissions = redisServer.getList(username.toUpperCase() + RedisUtils.PERMISSION_SUF);
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
            //刷新令牌
            if(JWTUtils.isRefresh(token)){
                String newToken = JWTUtils.refreshToken(token);
                response.addHeader("new-token",TokenConstants.PREFIX + newToken);
                response.addHeader("Access-Control-Expose-Headers", "new-token");
            }
            chain.doFilter(request, response);
        }catch (SignatureException e){
            RUtils.toResponse(R.fail("签名被篡改"),response);
        }catch (ExpiredJwtException e){
            RUtils.toResponse(R.fail(HttpStatus.UNAUTHORIZED,"登录已过期"),response);
        }catch (MalformedJwtException e){
            RUtils.toResponse(R.fail("无效令牌"),response);
        }catch (JSONException e) {
            e.printStackTrace();
            chain.doFilter(request, response);
        }catch (Exception e){
           // RUtils.toResponse(R.fail("解析失败"),response);
            chain.doFilter(request, response);
        }
    }
}

