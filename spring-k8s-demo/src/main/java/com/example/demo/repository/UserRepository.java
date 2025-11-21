package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository - User Data Access Repository
 * 用户仓库 - 用户数据访问仓库
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * Spring Data JPA repository interface for User entity.
 * Provides CRUD operations and custom query methods.
 * 
 * 用于 User 实体的 Spring Data JPA 仓库接口。
 * 提供 CRUD 操作和自定义查询方法。
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email address
     * 根据邮箱地址查找用户
     * 
     * @param email User email address / 用户邮箱地址
     * @return Optional containing user if found / 如果找到则包含用户的 Optional
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email address
     * 检查邮箱地址是否存在用户
     * 
     * @param email User email address / 用户邮箱地址
     * @return true if user exists, false otherwise / 如果用户存在则返回 true，否则返回 false
     */
    boolean existsByEmail(String email);
}

