package com.note.web.utils;

import com.note.api.constant.TokenConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * JWT工具类
 *
 * @date 2022/11/29 20:57
 **/
public class JWTUtils {
    private final static String key = TokenConstants.SECRET;
    private final static long expTime = 60 * 60 * 12 * 1000;

    /**
     * 创建令牌
     *
     * @param username 用户名
     * @return {@link String}
     */
    public static String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .signWith(SignatureAlgorithm.HS256, key).compact();
    }

    /**
     * 创建令牌
     *
     * @param username 用户名
     * @param data     数据
     * @return {@link String}
     */
    public static String createToken(String username, Object data) {
         return  Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .claim("data",data)
                .signWith(SignatureAlgorithm.HS256, key).compact();
    }

    /**
     * 用于刷新令牌时根据Claims创建新的令牌
     *
     * @param claims
     * @return {@link String}
     */
    public static String createToken(Claims claims) {
         return  Jwts.builder()
                 .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .signWith(SignatureAlgorithm.HS256, key).compact();
    }

    /**
     * 解析令牌
     *
     * @param token 令牌
     * @return {@link Claims}
     */
    public static Claims parToken(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    /**
     * 判断是否需要刷新令牌
     *
     * @param token 令牌
     * @return {@link Boolean}
     */
    public static Boolean isRefresh(String token){
        long expTime = parToken(token).getExpiration().getTime() / 60000;
        long currTime = new Date(System.currentTimeMillis()).getTime() / 60000;

        return expTime - currTime < 16;
    }

    /**
     * 刷新令牌
     *
     * @param oldToken 旧令牌
     * @return {@link String}
     */
    public static String refreshToken(String oldToken){
           return createToken(parToken(oldToken));
    }
}
