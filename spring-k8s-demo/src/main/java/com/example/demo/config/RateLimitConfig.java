package com.example.demo.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {
    
    /**
     * 配置 API 限流桶
     * 每秒钟允许 10 个请求，突发最多 20 个请求
     */
    @Bean
    public Bucket apiRateLimitBucket() {
        Refill refill = Refill.intervally(10, Duration.ofSeconds(1));
        Bandwidth limit = Bandwidth.classic(20, refill);
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }
    
    /**
     * 认证接口限流（更严格）
     * 每秒钟允许 5 个请求，突发最多 10 个请求
     */
    @Bean(name = "authRateLimitBucket")
    public Bucket authRateLimitBucket() {
        Refill refill = Refill.intervally(5, Duration.ofSeconds(1));
        Bandwidth limit = Bandwidth.classic(10, refill);
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }
}

