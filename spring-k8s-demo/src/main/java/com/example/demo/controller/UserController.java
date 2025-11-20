package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * UserController - User Management REST API Controller
 * 用户控制器 - 用户管理 REST API 控制器
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This controller provides RESTful endpoints for user management operations,
 * including CRUD (Create, Read, Update, Delete) operations.
 * All endpoints require JWT authentication (except public endpoints).
 * 
 * 该控制器提供用于用户管理操作的 RESTful 端点，
 * 包括 CRUD（创建、读取、更新、删除）操作。
 * 所有端点都需要 JWT 认证（公共端点除外）。
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    /**
     * UserService - Business logic for user operations
     * 用户服务 - 用户操作的业务逻辑
     */
    @Autowired
    private UserService userService;
    
    /**
     * Get all users
     * 获取所有用户
     * 
     * @GET /api/users
     * @return ResponseEntity containing list of all users / 包含所有用户列表的响应实体
     * @description
     * Retrieves all users from the database. Results are cached in Redis.
     * 从数据库检索所有用户。结果缓存在 Redis 中。
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", users);
        response.put("count", users.size());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get user by ID
     * 根据 ID 获取用户
     * 
     * @GET /api/users/{id}
     * @param id User ID / 用户 ID
     * @return ResponseEntity containing user data or error message / 包含用户数据或错误消息的响应实体
     * @description
     * Retrieves a specific user by ID. Result is cached in Redis.
     * 根据 ID 检索特定用户。结果缓存在 Redis 中。
     */
    @GetMapping("/{id}")
    @Cacheable(value = "user", key = "#id")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (user.isPresent()) {
            response.put("success", true);
            response.put("data", user.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "User not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * Create a new user
     * 创建新用户
     * 
     * @POST /api/users
     * @param user User object to create / 要创建的用户对象
     * @return ResponseEntity containing created user data or error message / 包含创建的用户数据或错误消息的响应实体
     * @description
     * Creates a new user. Validates input and checks for duplicate email.
     * Clears the users cache after creation.
     * 
     * 创建新用户。验证输入并检查重复邮箱。
     * 创建后清除用户缓存。
     */
    @PostMapping
    @CacheEvict(value = "users", key = "'all'")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User created successfully");
            response.put("data", createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Update an existing user
     * 更新现有用户
     * 
     * @PUT /api/users/{id}
     * @param id User ID to update / 要更新的用户 ID
     * @param userDetails Updated user data / 更新的用户数据
     * @return ResponseEntity containing updated user data or error message / 包含更新的用户数据或错误消息的响应实体
     * @description
     * Updates an existing user by ID. Validates input and checks for duplicate email.
     * 根据 ID 更新现有用户。验证输入并检查重复邮箱。
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, 
                                                          @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User updated successfully");
            response.put("data", updatedUser);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * Delete a user by ID
     * 根据 ID 删除用户
     * 
     * @DELETE /api/users/{id}
     * @param id User ID to delete / 要删除的用户 ID
     * @return ResponseEntity containing success message or error message / 包含成功消息或错误消息的响应实体
     * @description
     * Deletes a user by ID. Clears all user-related caches after deletion.
     * 根据 ID 删除用户。删除后清除所有与用户相关的缓存。
     */
    @DeleteMapping("/{id}")
    @CacheEvict(value = {"users", "user"}, allEntries = true)
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

