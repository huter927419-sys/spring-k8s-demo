package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtUtil - JWT Token Utility Class
 * JWT 工具类 - JWT 令牌工具类
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This utility class provides methods for JWT token generation, validation,
 * and claim extraction. It uses HMAC SHA-256 algorithm for signing.
 * 
 * 该工具类提供用于 JWT 令牌生成、验证和声明提取的方法。
 * 它使用 HMAC SHA-256 算法进行签名。
 */
@Component
public class JwtUtil {
    
    /**
     * JWT Secret Key - From application properties or Kubernetes Secret
     * JWT 密钥 - 来自应用程序属性或 Kubernetes Secret
     */
    @Value("${jwt.secret}")
    private String secret;
    
    /**
     * JWT Expiration Time - In milliseconds (default: 24 hours)
     * JWT 过期时间 - 以毫秒为单位（默认：24 小时）
     */
    @Value("${jwt.expiration}")
    private Long expiration;
    
    /**
     * Get signing key from secret string
     * 从密钥字符串获取签名密钥
     * 
     * @return SecretKey for JWT signing / 用于 JWT 签名的 SecretKey
     * @description
     * Converts the secret string into a SecretKey using HMAC SHA-256.
     * 使用 HMAC SHA-256 将密钥字符串转换为 SecretKey。
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    /**
     * Extract username (subject) from JWT token
     * 从 JWT 令牌提取用户名（主题）
     * 
     * @param token JWT token / JWT 令牌
     * @return Username (email) from token / 来自令牌的用户名（邮箱）
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extract expiration date from JWT token
     * 从 JWT 令牌提取过期日期
     * 
     * @param token JWT token / JWT 令牌
     * @return Expiration date / 过期日期
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extract a specific claim from JWT token
     * 从 JWT 令牌提取特定声明
     * 
     * @param <T> Type of claim value / 声明值的类型
     * @param token JWT token / JWT 令牌
     * @param claimsResolver Function to extract claim / 提取声明的函数
     * @return Claim value / 声明值
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extract all claims from JWT token
     * 从 JWT 令牌提取所有声明
     * 
     * @param token JWT token / JWT 令牌
     * @return Claims object / Claims 对象
     * @description
     * Parses and verifies the JWT token signature, then extracts all claims.
     * 解析并验证 JWT 令牌签名，然后提取所有声明。
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Check if token is expired
     * 检查令牌是否已过期
     * 
     * @param token JWT token / JWT 令牌
     * @return true if token is expired, false otherwise / 如果令牌已过期则返回 true，否则返回 false
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Generate JWT token for user
     * 为用户生成 JWT 令牌
     * 
     * @param username User email (used as subject) / 用户邮箱（用作主题）
     * @param role User role / 用户角色
     * @return JWT token string / JWT 令牌字符串
     * @description
     * Creates a JWT token with user email as subject and role as a claim.
     * 创建一个 JWT 令牌，用户邮箱作为主题，角色作为声明。
     */
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }
    
    /**
     * Create JWT token with claims and subject
     * 使用声明和主题创建 JWT 令牌
     * 
     * @param claims Additional claims to include / 要包含的附加声明
     * @param subject Token subject (usually username/email) / 令牌主题（通常是用户名/邮箱）
     * @return JWT token string / JWT 令牌字符串
     * @description
     * Builds a JWT token with specified claims, subject, issue time, and expiration.
     * 构建一个包含指定声明、主题、签发时间和过期时间的 JWT 令牌。
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Validate JWT token
     * 验证 JWT 令牌
     * 
     * @param token JWT token to validate / 要验证的 JWT 令牌
     * @param username Expected username (email) / 预期的用户名（邮箱）
     * @return true if token is valid and not expired, false otherwise
     *         如果令牌有效且未过期则返回 true，否则返回 false
     * @description
     * Validates token by checking if the extracted username matches the expected
     * username and if the token is not expired.
     * 
     * 通过检查提取的用户名是否与预期用户名匹配
     * 以及令牌是否未过期来验证令牌。
     */
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}

