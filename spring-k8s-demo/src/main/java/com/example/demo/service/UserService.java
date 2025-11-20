package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * UserService - User Business Logic Service
 * 用户服务 - 用户业务逻辑服务
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This service class provides business logic for user management operations.
 * It handles user CRUD operations with caching support and transaction management.
 * 
 * 该服务类提供用户管理操作的业务逻辑。
 * 它处理用户 CRUD 操作，支持缓存和事务管理。
 */
@Service
@Transactional
public class UserService {
    
    /**
     * UserRepository - Data access layer for User entity
     * 用户仓库 - 用户实体的数据访问层
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all users
     * 获取所有用户
     * 
     * @return List of all users / 所有用户的列表
     * @description
     * Retrieves all users from the database. Results are cached in Redis
     * with key "users::all" to improve performance.
     * 
     * 从数据库检索所有用户。结果缓存在 Redis 中，
     * 键为 "users::all" 以提高性能。
     */
    @Cacheable(value = "users", key = "'all'")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Get user by ID
     * 根据 ID 获取用户
     * 
     * @param id User ID / 用户 ID
     * @return Optional containing user if found / 如果找到则包含用户的 Optional
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Get user by email
     * 根据邮箱获取用户
     * 
     * @param email User email address / 用户邮箱地址
     * @return Optional containing user if found / 如果找到则包含用户的 Optional
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Create a new user
     * 创建新用户
     * 
     * @param user User object to create / 要创建的用户对象
     * @return Created user object / 创建的用户对象
     * @throws RuntimeException if email already exists / 如果邮箱已存在则抛出运行时异常
     * @description
     * Creates a new user after validating that the email is unique.
     * 在验证邮箱唯一后创建新用户。
     */
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }
    
    /**
     * Update an existing user
     * 更新现有用户
     * 
     * @param id User ID to update / 要更新的用户 ID
     * @param userDetails Updated user data / 更新的用户数据
     * @return Updated user object / 更新的用户对象
     * @throws RuntimeException if user not found or email already exists / 如果用户未找到或邮箱已存在则抛出运行时异常
     * @description
     * Updates user information. Validates that the new email (if changed) is unique.
     * 更新用户信息。验证新邮箱（如果更改）是否唯一。
     */
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Check if email is being changed and if new email already exists
        // 检查邮箱是否正在更改以及新邮箱是否已存在
        if (!user.getEmail().equals(userDetails.getEmail()) && 
            userRepository.existsByEmail(userDetails.getEmail())) {
            throw new RuntimeException("User with email " + userDetails.getEmail() + " already exists");
        }
        
        // Update user fields / 更新用户字段
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        
        return userRepository.save(user);
    }
    
    /**
     * Delete a user by ID
     * 根据 ID 删除用户
     * 
     * @param id User ID to delete / 要删除的用户 ID
     * @throws RuntimeException if user not found / 如果用户未找到则抛出运行时异常
     * @description
     * Deletes a user from the database.
     * 从数据库中删除用户。
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }
}

