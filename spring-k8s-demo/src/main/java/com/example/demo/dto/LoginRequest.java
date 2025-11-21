package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * LoginRequest - User Login Request DTO
 * 登录请求 DTO - 用户登录请求数据传输对象
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * Data Transfer Object for user login requests.
 * Contains email and password for authentication.
 * 
 * 用于用户登录请求的数据传输对象。
 * 包含用于认证的邮箱和密码。
 */
public class LoginRequest {
    
    /**
     * User email address - Required for login
     * 用户邮箱地址 - 登录必需
     */
    @NotBlank(message = "Email is required")
    private String email;
    
    /**
     * User password - Required for login
     * 用户密码 - 登录必需
     */
    @NotBlank(message = "Password is required")
    private String password;
    
    // Getters and Setters / Getter 和 Setter 方法
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}

