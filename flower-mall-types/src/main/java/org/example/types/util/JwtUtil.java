package org.example.types.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-12 22:45
 */
@Component
public class JwtUtil {
    private static final String SECRET_KEY = "flower-mall-secret-key-flower-mall-secret-key";
    private static final long TTL = 24 * 60 * 60 * 1000; // 24小时

    /**
     * 生成 Token
     * @param userId 用户ID
     * @param username 用户名
     * @return String
     */
    public String createToken(String userId, String username,String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TTL))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

        return builder.compact();
    }

    /**
     * 解析 Token
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 校验 Token 是否有效（是否过期等由 parseToken 抛出异常处理）
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
