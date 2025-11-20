package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * User Entity - User Domain Model
 * 用户实体 - 用户领域模型
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This entity represents a user in the system. It includes user information,
 * authentication credentials, role, and audit timestamps.
 * 
 * 该实体表示系统中的用户。它包括用户信息、
 * 认证凭据、角色和审计时间戳。
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email"),        // Index for email lookup / 邮箱查找索引
    @Index(name = "idx_created_at", columnList = "created_at") // Index for date queries / 日期查询索引
})
public class User {
    
    /**
     * User ID - Primary key, auto-generated
     * 用户 ID - 主键，自动生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User name - Required, 2-50 characters
     * 用户姓名 - 必填，2-50 个字符
     */
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(nullable = false, length = 50)
    private String name;
    
    /**
     * User email - Required, unique, validated format
     * 用户邮箱 - 必填，唯一，已验证格式
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    /**
     * User phone number - Optional, max 20 characters
     * 用户电话号码 - 可选，最多 20 个字符
     */
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Column(length = 20)
    private String phone;
    
    /**
     * User password - Hashed password (BCrypt)
     * 用户密码 - 哈希密码（BCrypt）
     */
    @Column(nullable = false)
    private String password;
    
    /**
     * User role - Default is USER
     * 用户角色 - 默认为 USER
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
    /**
     * Creation timestamp - Auto-set on creation, not updatable
     * 创建时间戳 - 创建时自动设置，不可更新
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Last update timestamp - Auto-updated on modification
     * 最后更新时间戳 - 修改时自动更新
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Pre-persist callback - Set timestamps before saving
     * 持久化前回调 - 保存前设置时间戳
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Pre-update callback - Update timestamp before modification
     * 更新前回调 - 修改前更新时间戳
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
}

