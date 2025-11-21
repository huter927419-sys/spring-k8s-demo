package com.example.demo.entity;

/**
 * Role - User Role Enumeration
 * 角色枚举 - 用户角色枚举
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This enum defines the available user roles in the system.
 * Used for role-based access control (RBAC).
 * 
 * 该枚举定义了系统中可用的用户角色。
 * 用于基于角色的访问控制（RBAC）。
 */
public enum Role {
    /**
     * Regular user role
     * 普通用户角色
     */
    USER,
    
    /**
     * Administrator role with full access
     * 管理员角色，拥有完全访问权限
     */
    ADMIN
}

