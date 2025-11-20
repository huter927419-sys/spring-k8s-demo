package com.example.demo.filter;

import com.example.demo.service.AuthService;
import com.example.demo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JwtAuthenticationFilter - JWT Authentication Filter
 * JWT 认证过滤器
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This filter intercepts HTTP requests to validate JWT tokens and set up
 * Spring Security authentication context. It runs after RateLimitFilter (Order 1).
 * 
 * 该过滤器拦截 HTTP 请求以验证 JWT 令牌并设置
 * Spring Security 认证上下文。它在 RateLimitFilter（Order 1）之后运行。
 */
@Component
@Order(2) // Execute after RateLimitFilter / 在 RateLimitFilter 之后执行
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    /**
     * JwtUtil - Utility for JWT token operations
     * JWT 工具类 - 用于 JWT 令牌操作
     */
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * AuthService - Service for token validation
     * 认证服务 - 用于令牌验证
     */
    @Autowired
    private AuthService authService;
    
    /**
     * Filter internal method - Process JWT authentication
     * 过滤器内部方法 - 处理 JWT 认证
     * 
     * @param request HTTP servlet request / HTTP servlet 请求
     * @param response HTTP servlet response / HTTP servlet 响应
     * @param chain Filter chain / 过滤器链
     * @throws ServletException if servlet error occurs / 如果发生 servlet 错误
     * @throws IOException if I/O error occurs / 如果发生 I/O 错误
     * @description
     * Extracts JWT token from Authorization header, validates it, and sets up
     * Spring Security authentication context if valid. Skips authentication for
     * /api/auth/** endpoints.
     * 
     * 从 Authorization 头提取 JWT 令牌，验证它，如果有效则设置
     * Spring Security 认证上下文。跳过 /api/auth/** 端点的认证。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        // Skip authentication endpoints - These endpoints don't require JWT token
        // 跳过认证端点 - 这些端点不需要 JWT 令牌
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/")) {
            chain.doFilter(request, response);
            return;
        }
        
        // Extract JWT token from Authorization header
        // 从 Authorization 头提取 JWT 令牌
        final String authHeader = request.getHeader("Authorization");
        
        String email = null;
        String jwt = null;
        
        // Parse Bearer token / 解析 Bearer 令牌
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Remove "Bearer " prefix / 移除 "Bearer " 前缀
            try {
                email = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.error("JWT token extraction failed", e);
            }
        }
        
        // Validate token and set authentication context
        // 验证令牌并设置认证上下文
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (authService.validateToken(jwt)) {
                try {
                    // Extract role from token / 从令牌提取角色
                    String role = jwtUtil.extractClaim(jwt, claims -> claims.get("role", String.class));
                    
                    // Create authentication token / 创建认证令牌
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                    );
                    
                    // Set authentication details / 设置认证详情
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } catch (Exception e) {
                    logger.error("Cannot set user authentication", e);
                }
            }
        }
        
        // Continue filter chain / 继续过滤器链
        chain.doFilter(request, response);
    }
}

