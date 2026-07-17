/**
 * JWT 工具类
 * 提供 JWT token 的生成、解析和验证功能
 * 
 * JWT (JSON Web Token) 是一种用于安全传输信息的开放标准（RFC 7519）
 * 由三部分组成:
 * - Header: 声明token类型和加密算法
 * - Payload: 存储用户信息等声明
 * - Signature: 签名，用于验证token的真实性
 */
package com.example.gchc.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @Component 注解标识这是一个 Spring 组件
 * Spring 会自动扫描并创建该类的实例
 */
@Component
public class JwtUtil {

    /**
     * JWT 签名密钥，从配置文件读取
     * 必须保密，用于生成和验证 token 的签名
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * token 过期时间（毫秒），从配置文件读取
     * 例如: 86400000 表示 24 小时
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 获取签名密钥
     * 将字符串密钥转换为 HMAC-SHA 算法所需的 SecretKey 对象
     * 
     * @return SecretKey 签名密钥
     */
    private SecretKey getSigningKey() {
        // 将密钥字符串转换为字节数组
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        // 使用 HMAC-SHA 算法生成密钥
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 从 token 中提取用户名
     * 
     * @param token JWT token
     * @return 用户名
     */
    public String extractUsername(String token) {
        // Claims::getSubject 是一个方法引用，用于获取 token 的主题（subject）
        // 这里主题存储的是用户名
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从 token 中提取过期时间
     * 
     * @param token JWT token
     * @return 过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 从 token 中提取指定的声明（claim）
     * 使用泛型和函数式接口实现灵活的声明提取
     * 
     * @param token JWT token
     * @param claimsResolver 用于提取声明的函数
     * @param <T> 返回值类型
     * @return 提取的声明值
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // 先解析 token 获取所有声明
        final Claims claims = extractAllClaims(token);
        // 使用传入的函数提取指定的声明
        return claimsResolver.apply(claims);
    }

    /**
     * 解析 token 获取所有声明（Claims）
     * 
     * @param token JWT token
     * @return Claims 对象，包含所有声明信息
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()                    // 创建 JWT 解析器
                .verifyWith(getSigningKey())    // 设置签名密钥用于验证
                .build()                        // 构建解析器
                .parseSignedClaims(token)       // 解析 token
                .getPayload();                  // 获取 payload（包含所有声明）
    }

    /**
     * 判断 token 是否过期
     * 
     * @param token JWT token
     * @return true 如果已过期，false 否则
     */
    public Boolean isTokenExpired(String token) {
        // 比较 token 的过期时间和当前时间
        return extractExpiration(token).before(new Date());
    }

    /**
     * 根据用户详情生成 token
     * 
     * @param userDetails Spring Security 的用户详情对象
     * @return JWT token
     */
    public String generateToken(UserDetails userDetails) {
        // 创建空的声明 map（可以添加额外信息）
        Map<String, Object> claims = new HashMap<>();
        // 使用用户名作为主题生成 token
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * 根据用户名和角色生成 token
     * 
     * @param username 用户名
     * @param role 用户角色
     * @return JWT token
     */
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    /**
     * 从 token 中提取角色
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    /**
     * 创建 JWT token
     * 
     * @param claims 自定义声明（可添加额外信息）
     * @param subject 主题（通常是用户名）
     * @return JWT token 字符串
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()                        // 创建 JWT 构建器
                .claims(claims)                      // 设置自定义声明
                .subject(subject)                    // 设置主题（用户名）
                .issuedAt(new Date(System.currentTimeMillis()))  // 设置签发时间
                .expiration(new Date(System.currentTimeMillis() + expiration))  // 设置过期时间
                .signWith(getSigningKey())           // 使用密钥签名
                .compact();                          // 压缩生成最终的 token 字符串
    }

    /**
     * 验证 token 是否有效
     * 验证逻辑:
     * 1. token 中的用户名与用户详情中的用户名匹配
     * 2. token 未过期
     * 
     * @param token JWT token
     * @param userDetails 用户详情
     * @return true 如果 token 有效，false 否则
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        // 从 token 中提取用户名
        final String username = extractUsername(token);
        // 验证用户名匹配且 token 未过期
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 获取 token 过期时间（秒）
     * 
     * @return 过期时间（秒）
     */
    public Long getExpirationInSeconds() {
        return expiration / 1000;
    }
}
