package com.aoyu.bitsetup.common.utils;

import com.aoyu.bitsetup.common.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName：JwtUtils
 * @Author: aoyu
 * @Date: 2025-04-07 14:08
 * @Description: jwt工具类
 */
@Component
public class JwtUtil {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 生成密钥
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    /**
     * 解析过期时间配置
     */
    private long parseExpiration() {
        String expiration = jwtProperties.getExpiration();
        if (expiration.endsWith("d")) {
            return Duration.ofDays(Long.parseLong(expiration.substring(0, expiration.length() - 1))).toMillis();
        } else if (expiration.endsWith("h")) {
            return Duration.ofHours(Long.parseLong(expiration.substring(0, expiration.length() - 1))).toMillis();
        } else if (expiration.endsWith("m")) {
            return Duration.ofMinutes(Long.parseLong(expiration.substring(0, expiration.length() - 1))).toMillis();
        } else {
            // 默认按毫秒处理
            return Long.parseLong(expiration);
        }
    }

    /**
     * 生成Token
     * @param subject 主题（通常是用户ID或用户名）
     * @param claims 自定义声明
     * @return JWT Token
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + parseExpiration());

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512);

        return builder.compact();
    }

    /**
     * 生成Token（无自定义声明）
     * @param subject 主题
     * @return JWT Token
     */
    public String generateToken(String subject) {
        return generateToken(subject, null);
    }

    /**
     * 验证Token是否有效
     * @param token JWT Token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断Token是否过期
     * @param token JWT Token
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 获取Token的过期时间
     * @param token JWT Token
     * @return 过期时间
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 从Token中获取主题（通常是用户ID或用户名）
     * @param token JWT Token
     * @return 主题
     */
    public String getSubjectFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 从Token中获取签发时间
     * @param token JWT Token
     * @return 签发时间
     */
    public Date getIssuedAtFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getIssuedAt();
    }

    /**
     * 从Token中获取自定义声明
     * @param token JWT Token
     * @param key 声明的键
     * @return 声明的值
     */
    public Object getClaimFromToken(String token, String key) {
        Claims claims = getClaimsFromToken(token);
        return claims.get(key);
    }

    /**
     * 从Token中获取所有Claims
     * @param token JWT Token
     * @return Claims对象
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("无法解析JWT Token", e);
        }
    }

    /**
     * 刷新Token（基于现有Token生成新Token）
     * @param token 原始Token
     * @return 新的Token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String subject = claims.getSubject();

        // 移除时间相关的claims，让新token重新生成
        claims.remove(Claims.ISSUED_AT);
        claims.remove(Claims.EXPIRATION);

        return generateToken(subject, claims);
    }
}