package com.example.demo.dto;

/**
 * JwtResponse - JWT Authentication Response DTO
 * JWT 响应 DTO - JWT 认证响应数据传输对象
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * Data Transfer Object for JWT authentication responses.
 * Contains the JWT token and user information returned after successful login/registration.
 * 
 * 用于 JWT 认证响应的数据传输对象。
 * 包含登录/注册成功后返回的 JWT 令牌和用户信息。
 */
public class JwtResponse {
    /**
     * JWT authentication token
     * JWT 认证令牌
     */
    private String token;
    
    /**
     * Token type - Default is "Bearer"
     * 令牌类型 - 默认为 "Bearer"
     */
    private String type = "Bearer";
    
    /**
     * User ID
     * 用户 ID
     */
    private Long id;
    
    /**
     * User email address
     * 用户邮箱地址
     */
    private String email;
    
    /**
     * User name
     * 用户姓名
     */
    private String name;
    
    /**
     * User role (USER or ADMIN)
     * 用户角色（USER 或 ADMIN）
     */
    private String role;
    
    /**
     * Constructor for JwtResponse
     * JwtResponse 构造函数
     * 
     * @param token JWT token / JWT 令牌
     * @param id User ID / 用户 ID
     * @param email User email / 用户邮箱
     * @param name User name / 用户姓名
     * @param role User role / 用户角色
     */
    public JwtResponse(String token, Long id, String email, String name, String role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}

