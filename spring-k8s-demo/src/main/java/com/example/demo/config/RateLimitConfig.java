package com.example.demo.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * RateLimitConfig - API Rate Limiting Configuration
 * 速率限制配置 - API 速率限制配置
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This configuration class sets up rate limiting buckets using Bucket4j.
 * Different rate limits are applied for general API endpoints and authentication endpoints.
 * 
 * 该配置类使用 Bucket4j 设置速率限制桶。
 * 对通用 API 端点和认证端点应用不同的速率限制。
 */
@Configuration
public class RateLimitConfig {
    
    /**
     * Configure API rate limit bucket for general endpoints
     * 为通用端点配置 API 速率限制桶
     * 
     * @return Bucket instance with rate limiting configuration / 带有速率限制配置的 Bucket 实例
     * @description
     * Rate limit: 10 requests per second, burst capacity: 20 requests
     * Used for general API endpoints like /api/users
     * 
     * 速率限制：每秒 10 个请求，突发容量：20 个请求
     * 用于通用 API 端点，如 /api/users
     */
    @Bean
    public Bucket apiRateLimitBucket() {
        // Refill 10 tokens every second / 每秒补充 10 个令牌
        Refill refill = Refill.intervally(10, Duration.ofSeconds(1));
        // Maximum capacity: 20 tokens / 最大容量：20 个令牌
        Bandwidth limit = Bandwidth.classic(20, refill);
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }
    
    /**
     * Configure authentication rate limit bucket (stricter)
     * 配置认证速率限制桶（更严格）
     * 
     * @return Bucket instance with stricter rate limiting / 带有更严格速率限制的 Bucket 实例
     * @description
     * Rate limit: 5 requests per second, burst capacity: 10 requests
     * Used for authentication endpoints like /api/auth/login, /api/auth/register
     * Stricter limits to prevent brute force attacks
     * 
     * 速率限制：每秒 5 个请求，突发容量：10 个请求
     * 用于认证端点，如 /api/auth/login、/api/auth/register
     * 更严格的限制以防止暴力破解攻击
     */
    @Bean(name = "authRateLimitBucket")
    public Bucket authRateLimitBucket() {
        // Refill 5 tokens every second / 每秒补充 5 个令牌
        Refill refill = Refill.intervally(5, Duration.ofSeconds(1));
        // Maximum capacity: 10 tokens / 最大容量：10 个令牌
        Bandwidth limit = Bandwidth.classic(10, refill);
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }
}

