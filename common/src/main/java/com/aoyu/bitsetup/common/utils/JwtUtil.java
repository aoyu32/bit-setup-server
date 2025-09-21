package com.aoyu.bitsetup.common.utils;

import com.aoyu.bitsetup.common.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName：JwtUtil
 * @Author: aoyu
 * @Date: 2025-09-20 13:37
 * @Description: jwt工具类
 */



@Component
public class JwtUtil {

    @Autowired
    private JwtProperties jwtProperties;

    // 从 JWT 中提取所有声明
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    // 获取签名密钥
    private Key getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    // 从 JWT 中提取特定声明
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 获取用户名（subject）从 JWT
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 获取 JWT 的过期时间
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 检查 JWT 是否过期
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // 生成 JWT 令牌
    public String generateToken(String value, Map<String, Object> claims) {
        return doGenerateToken(claims, value);
    }

    // 生成 JWT 令牌的核心方法
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        long expirationTimeMillis = parseExpirationTime(jwtProperties.getExpiration());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .signWith(SignatureAlgorithm.HS512, getSigningKey())
                .compact();
    }

    // 验证 JWT 令牌
    public Boolean validateToken(String token, String username) {
        final String usernameFromToken = getUsernameFromToken(token);
        return (usernameFromToken.equals(username) && !isTokenExpired(token));
    }

    // 解析过期时间（支持格式如 "7d", "1h", "30m"）
    private long parseExpirationTime(String expirationTime) {
        expirationTime = expirationTime.toLowerCase();
        if (expirationTime.endsWith("d")) {
            // 天数转换为毫秒
            return Long.parseLong(expirationTime.replace("d", "")) * 24 * 60 * 60 * 1000;
        } else if (expirationTime.endsWith("h")) {
            // 小时转换为毫秒
            return Long.parseLong(expirationTime.replace("h", "")) * 60 * 60 * 1000;
        } else if (expirationTime.endsWith("m")) {
            // 分钟转换为毫秒
            return Long.parseLong(expirationTime.replace("m", "")) * 60 * 1000;
        } else if (expirationTime.endsWith("s")) {
            // 秒转换为毫秒
            return Long.parseLong(expirationTime.replace("s", "")) * 1000;
        } else {
            // 默认按毫秒处理
            return Long.parseLong(expirationTime);
        }
    }

    // 示例：生成带自定义声明的 JWT 令牌
    public String generateTokenWithClaims(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return generateToken(username, claims);
    }
}