package com.example.demo.config;

import com.example.demo.filter.JwtAuthenticationFilter;
import com.example.demo.filter.RateLimitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * SecurityConfig - Spring Security Configuration
 * 安全配置 - Spring Security 配置
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This configuration class sets up Spring Security with JWT authentication,
 * rate limiting, CORS, and stateless session management.
 * 
 * 该配置类设置 Spring Security，包括 JWT 认证、
 * 速率限制、CORS 和无状态会话管理。
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    /**
     * JWT Authentication Filter - Validates JWT tokens in requests
     * JWT 认证过滤器 - 验证请求中的 JWT 令牌
     */
    @Autowired
    @Lazy
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /**
     * Rate Limit Filter - Implements API rate limiting
     * 速率限制过滤器 - 实现 API 速率限制
     */
    @Autowired
    private RateLimitFilter rateLimitFilter;
    
    /**
     * Configure AuthenticationManager bean
     * 配置 AuthenticationManager Bean
     * 
     * @param config AuthenticationConfiguration / 认证配置
     * @return AuthenticationManager instance / AuthenticationManager 实例
     * @throws Exception if configuration fails / 如果配置失败
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    /**
     * Configure Spring Security filter chain
     * 配置 Spring Security 过滤器链
     * 
     * @param http HttpSecurity builder / HttpSecurity 构建器
     * @return SecurityFilterChain / 安全过滤器链
     * @throws Exception if configuration fails / 如果配置失败
     * @description
     * Configures:
     * - CSRF disabled (using JWT instead)
     * - CORS enabled
     * - Stateless session management
     * - Public endpoints: /api/auth/**, /actuator/**, /error
     * - Protected endpoints: all other /api/** endpoints
     * - Custom exception handlers for authentication and authorization
     * - Filter order: RateLimitFilter -> JwtAuthenticationFilter
     * 
     * 配置：
     * - 禁用 CSRF（使用 JWT 代替）
     * - 启用 CORS
     * - 无状态会话管理
     * - 公共端点：/api/auth/**, /actuator/**, /error
     * - 受保护端点：所有其他 /api/** 端点
     * - 自定义异常处理器用于认证和授权
     * - 过滤器顺序：RateLimitFilter -> JwtAuthenticationFilter
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - No authentication required
                // 公共端点 - 不需要认证
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/hello", "/api/info", "/api/health").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/error").permitAll()
                // Protected endpoints - Require authentication
                // 受保护端点 - 需要认证
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                // Authentication entry point - Handle 401 Unauthorized
                // 认证入口点 - 处理 401 未授权
                .authenticationEntryPoint((request, response, authException) -> {
                    String path = request.getRequestURI();
                    // For /api/auth/** paths, should not trigger authentication failure
                    // 对于 /api/auth/** 路径，不应该触发认证失败
                    if (path.startsWith("/api/auth/")) {
                        // Allow processing to continue, should not intercept here
                        // 允许继续处理，不应该在这里拦截
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Unauthorized\"}");
                    }
                })
                // Access denied handler - Handle 403 Forbidden
                // 访问拒绝处理器 - 处理 403 禁止访问
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"Access Denied\"}");
                })
            );
        
        // Add filters in order: RateLimitFilter -> JwtAuthenticationFilter
        // 按顺序添加过滤器：RateLimitFilter -> JwtAuthenticationFilter
        // Note: Rate limit filter should be before JWT filter, but before authentication
        // 注意：速率限制过滤器应该在 JWT 过滤器之前，但在认证之前
        http.addFilterBefore(rateLimitFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    /**
     * Configure CORS (Cross-Origin Resource Sharing)
     * 配置 CORS（跨源资源共享）
     * 
     * @return CorsConfigurationSource / CORS 配置源
     * @description
     * Allows requests from specified origins with credentials support.
     * 允许来自指定来源的请求，支持凭据。
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allowed origins / 允许的来源
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",  // Development / 开发环境
            "http://localhost:5173",  // Vite dev server / Vite 开发服务器
            "http://localhost:8080",  // Local backend / 本地后端
            "https://flashswap09.online",  // Production HTTPS / 生产环境 HTTPS
            "http://flashswap09.online"    // Production HTTP / 生产环境 HTTP
        ));
        // Allowed HTTP methods / 允许的 HTTP 方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allowed headers / 允许的头
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Allow credentials (cookies, authorization headers) / 允许凭据（cookie、授权头）
        configuration.setAllowCredentials(true);
        // Cache preflight response for 1 hour / 缓存预检响应 1 小时
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

