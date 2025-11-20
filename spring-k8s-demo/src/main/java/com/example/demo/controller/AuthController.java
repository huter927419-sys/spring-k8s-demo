package com.example.demo.controller;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthController - Authentication REST API Controller
 * 认证控制器 - 认证 REST API 控制器
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This controller provides RESTful endpoints for user authentication operations,
 * including registration, login, logout, and token validation.
 * All endpoints are public and do not require authentication.
 * 
 * 该控制器提供用于用户认证操作的 RESTful 端点，
 * 包括注册、登录、退出登录和令牌验证。
 * 所有端点都是公共的，不需要认证。
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    /**
     * AuthService - Business logic for authentication operations
     * 认证服务 - 认证操作的业务逻辑
     */
    @Autowired
    private AuthService authService;
    
    /**
     * User registration endpoint
     * 用户注册端点
     * 
     * @POST /api/auth/register
     * @param request Registration request with user details / 包含用户详情的注册请求
     * @return ResponseEntity containing JWT token and user info / 包含 JWT 令牌和用户信息的响应实体
     * @description
     * Registers a new user and returns a JWT token for immediate authentication.
     * 注册新用户并返回 JWT 令牌以立即进行认证。
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            JwtResponse response = authService.register(request);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "User registered successfully");
            result.put("data", response);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    /**
     * User login endpoint
     * 用户登录端点
     * 
     * @POST /api/auth/login
     * @param request Login request with email and password / 包含邮箱和密码的登录请求
     * @return ResponseEntity containing JWT token and user info / 包含 JWT 令牌和用户信息的响应实体
     * @description
     * Authenticates user credentials and returns a JWT token.
     * 验证用户凭据并返回 JWT 令牌。
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        try {
            JwtResponse response = authService.login(request);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Login successful");
            result.put("data", response);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    /**
     * User logout endpoint
     * 用户退出登录端点
     * 
     * @POST /api/auth/logout
     * @param authHeader Authorization header containing Bearer token / 包含 Bearer 令牌的授权头
     * @return ResponseEntity containing logout status / 包含退出登录状态的响应实体
     * @description
     * Logs out the user by invalidating the JWT token.
     * 通过使 JWT 令牌无效来退出用户登录。
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            // Remove "Bearer " prefix from token / 从令牌中移除 "Bearer " 前缀
            String token = authHeader.substring(7);
            authService.logout(token);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Logout successful");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Logout failed");
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    /**
     * Token validation endpoint
     * 令牌验证端点
     * 
     * @GET /api/auth/validate
     * @param authHeader Authorization header containing Bearer token / 包含 Bearer 令牌的授权头
     * @return ResponseEntity containing token validation result / 包含令牌验证结果的响应实体
     * @description
     * Validates if a JWT token is still valid and not expired.
     * 验证 JWT 令牌是否仍然有效且未过期。
     */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            // Remove "Bearer " prefix from token / 从令牌中移除 "Bearer " 前缀
            String token = authHeader.substring(7);
            Boolean isValid = authService.validateToken(token);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("valid", isValid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("valid", false);
            return ResponseEntity.ok(result);
        }
    }
}

