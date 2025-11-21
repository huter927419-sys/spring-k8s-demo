package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoderConfig - Password Encoding Configuration
 * 密码编码器配置 - 密码编码配置
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This configuration class provides a BCrypt password encoder bean.
 * BCrypt is used for securely hashing user passwords before storage.
 * 
 * 该配置类提供 BCrypt 密码编码器 Bean。
 * BCrypt 用于在存储前安全地哈希用户密码。
 */
@Configuration
public class PasswordEncoderConfig {
    
    /**
     * Configure BCrypt password encoder
     * 配置 BCrypt 密码编码器
     * 
     * @return PasswordEncoder instance using BCrypt / 使用 BCrypt 的 PasswordEncoder 实例
     * @description
     * BCrypt is a strong, adaptive hashing algorithm that automatically handles salt generation.
     * Default strength is 10, which provides good security with reasonable performance.
     * 
     * BCrypt 是一种强大的自适应哈希算法，自动处理盐值生成。
     * 默认强度为 10，在合理性能下提供良好的安全性。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

