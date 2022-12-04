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
    private final static long expTime = 60 * 60 * 24 * 7 * 1000;

    public static String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .signWith(SignatureAlgorithm.HS256, key).compact();
    }

    public static String createToken(String username, Object data) {
         return  Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .claim("data",data)
                .signWith(SignatureAlgorithm.HS256, key).compact();
    }

    public static Claims parToken(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }
}
