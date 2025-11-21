package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * RegisterRequest - User Registration Request DTO
 * 注册请求 DTO - 用户注册请求数据传输对象
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * Data Transfer Object for user registration requests.
 * Contains user information for account creation with validation.
 * 
 * 用于用户注册请求的数据传输对象。
 * 包含用于创建账户的用户信息，带验证。
 */
public class RegisterRequest {
    
    /**
     * User name - Required, 2-50 characters
     * 用户姓名 - 必填，2-50 个字符
     */
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    
    /**
     * User email address - Required, must be valid email format
     * 用户邮箱地址 - 必填，必须是有效的邮箱格式
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    /**
     * User password - Required, minimum 6 characters
     * 用户密码 - 必填，最少 6 个字符
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    /**
     * User phone number - Optional, maximum 20 characters
     * 用户电话号码 - 可选，最多 20 个字符
     */
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
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
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
}

