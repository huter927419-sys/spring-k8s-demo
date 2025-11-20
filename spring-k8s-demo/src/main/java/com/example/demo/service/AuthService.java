package com.example.demo.service;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * AuthService - Authentication Business Logic Service
 * 认证服务 - 认证业务逻辑服务
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This service handles user authentication operations including registration,
 * login, logout, and token validation. It integrates with Redis for token storage
 * and uses JWT for stateless authentication.
 * 
 * 该服务处理用户认证操作，包括注册、登录、退出登录和令牌验证。
 * 它与 Redis 集成用于令牌存储，并使用 JWT 进行无状态认证。
 */
@Service
@Transactional
public class AuthService {
    
    /**
     * UserRepository - Data access layer for User entity
     * 用户仓库 - 用户实体的数据访问层
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * PasswordEncoder - BCrypt password encoder
     * 密码编码器 - BCrypt 密码编码器
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * JwtUtil - JWT token utility
     * JWT 工具类 - JWT 令牌工具
     */
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * RedisTemplate - Redis operations template
     * Redis 模板 - Redis 操作模板
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    /**
     * Redis key prefix for JWT tokens
     * Redis 键前缀用于 JWT 令牌
     */
    private static final String REDIS_TOKEN_PREFIX = "jwt:token:";
    
    /**
     * Redis key prefix for user information
     * Redis 键前缀用于用户信息
     */
    private static final String REDIS_USER_PREFIX = "jwt:user:";
    
    /**
     * Register a new user
     * 注册新用户
     * 
     * @param request Registration request with user details / 包含用户详情的注册请求
     * @return JwtResponse containing JWT token and user information / 包含 JWT 令牌和用户信息的响应
     * @throws RuntimeException if email already exists / 如果邮箱已存在则抛出运行时异常
     * @description
     * Creates a new user account, hashes the password, generates a JWT token,
     * and stores the token in Redis for session management.
     * 
     * 创建新用户账户，哈希密码，生成 JWT 令牌，
     * 并将令牌存储在 Redis 中用于会话管理。
     */
    public JwtResponse register(RegisterRequest request) {
        // Check if email already exists / 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        
        // Create new user / 创建新用户
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password / 哈希密码
        user.setPhone(request.getPhone());
        user.setRole(Role.USER);
        
        user = userRepository.save(user);
        
        // Generate JWT token / 生成 JWT 令牌
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        
        // Store token in Redis with 24-hour expiration
        // 将令牌存储在 Redis 中，24 小时过期
        String redisKey = REDIS_TOKEN_PREFIX + token;
        redisTemplate.opsForValue().set(redisKey, user.getEmail(), 24, TimeUnit.HOURS);
        
        // Store user info in Redis for quick lookup
        // 将用户信息存储在 Redis 中以便快速查找
        String userKey = REDIS_USER_PREFIX + user.getEmail();
        redisTemplate.opsForValue().set(userKey, user.getId().toString(), 24, TimeUnit.HOURS);
        
        return new JwtResponse(token, user.getId(), user.getEmail(), user.getName(), user.getRole().name());
    }
    
    /**
     * Authenticate user and generate JWT token
     * 认证用户并生成 JWT 令牌
     * 
     * @param request Login request with email and password / 包含邮箱和密码的登录请求
     * @return JwtResponse containing JWT token and user information / 包含 JWT 令牌和用户信息的响应
     * @throws RuntimeException if credentials are invalid / 如果凭据无效则抛出运行时异常
     * @description
     * Validates user credentials, generates a JWT token, and stores it in Redis.
     * 验证用户凭据，生成 JWT 令牌，并将其存储在 Redis 中。
     */
    public JwtResponse login(LoginRequest request) {
        // Find user by email / 根据邮箱查找用户
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        
        // Verify password / 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        
        // Generate JWT token / 生成 JWT 令牌
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        
        // Store token in Redis with 24-hour expiration
        // 将令牌存储在 Redis 中，24 小时过期
        String redisKey = REDIS_TOKEN_PREFIX + token;
        redisTemplate.opsForValue().set(redisKey, user.getEmail(), 24, TimeUnit.HOURS);
        
        // Store user info in Redis / 将用户信息存储在 Redis 中
        String userKey = REDIS_USER_PREFIX + user.getEmail();
        redisTemplate.opsForValue().set(userKey, user.getId().toString(), 24, TimeUnit.HOURS);
        
        return new JwtResponse(token, user.getId(), user.getEmail(), user.getName(), user.getRole().name());
    }
    
    /**
     * Logout user by invalidating token
     * 通过使令牌无效来退出用户登录
     * 
     * @param token JWT token to invalidate / 要使其无效的 JWT 令牌
     * @description
     * Removes the token and user info from Redis to invalidate the session.
     * 从 Redis 中移除令牌和用户信息以使会话无效。
     */
    public void logout(String token) {
        // Remove token from Redis / 从 Redis 中移除令牌
        String redisKey = REDIS_TOKEN_PREFIX + token;
        redisTemplate.delete(redisKey);
        
        // Get user email from token and remove user info
        // 从令牌获取用户邮箱并移除用户信息
        try {
            String email = jwtUtil.extractUsername(token);
            String userKey = REDIS_USER_PREFIX + email;
            redisTemplate.delete(userKey);
        } catch (Exception e) {
            // Token might be invalid, ignore error
            // 令牌可能无效，忽略错误
        }
    }
    
    /**
     * Validate JWT token
     * 验证 JWT 令牌
     * 
     * @param token JWT token to validate / 要验证的 JWT 令牌
     * @return true if token is valid and exists in Redis, false otherwise
     *         如果令牌有效且存在于 Redis 中则返回 true，否则返回 false
     * @description
     * Validates token by checking:
     * 1. Token exists in Redis
     * 2. Token email matches stored email
     * 3. Token is not expired (via JwtUtil)
     * 
     * 通过检查以下内容来验证令牌：
     * 1. 令牌存在于 Redis 中
     * 2. 令牌邮箱与存储的邮箱匹配
     * 3. 令牌未过期（通过 JwtUtil）
     */
    public Boolean validateToken(String token) {
        try {
            String email = jwtUtil.extractUsername(token);
            String redisKey = REDIS_TOKEN_PREFIX + token;
            String storedEmail = redisTemplate.opsForValue().get(redisKey);
            
            // Check if token exists in Redis and matches email, and is not expired
            // 检查令牌是否存在于 Redis 中、是否匹配邮箱，以及是否未过期
            return storedEmail != null && storedEmail.equals(email) && jwtUtil.validateToken(token, email);
        } catch (Exception e) {
            return false;
        }
    }
}

