package com.example.demo.filter;

import com.example.demo.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * RateLimitFilter - API Rate Limiting Filter
 * 速率限制过滤器 - API 速率限制过滤器
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This filter implements API rate limiting using Bucket4j. It applies different
 * rate limits for authentication endpoints (stricter) and general API endpoints.
 * It runs before JwtAuthenticationFilter (Order 1).
 * 
 * 该过滤器使用 Bucket4j 实现 API 速率限制。它对认证端点（更严格）
 * 和通用 API 端点应用不同的速率限制。它在 JwtAuthenticationFilter（Order 1）之前运行。
 */
@Component
@Order(1) // Execute before JWT authentication filter / 在 JWT 认证过滤器之前执行
public class RateLimitFilter implements Filter {
    
    /**
     * API Rate Limit Bucket - For general API endpoints
     * API 速率限制桶 - 用于通用 API 端点
     * Default: 10 requests/second, burst: 20
     * 默认：10 请求/秒，突发：20
     */
    @Autowired
    private Bucket apiRateLimitBucket;
    
    /**
     * Auth Rate Limit Bucket - For authentication endpoints (stricter)
     * 认证速率限制桶 - 用于认证端点（更严格）
     * Default: 5 requests/second, burst: 10
     * 默认：5 请求/秒，突发：10
     */
    @Autowired
    @Qualifier("authRateLimitBucket")
    private Bucket authRateLimitBucket;
    
    /**
     * Filter method - Apply rate limiting
     * 过滤器方法 - 应用速率限制
     * 
     * @param request Servlet request / Servlet 请求
     * @param response Servlet response / Servlet 响应
     * @param chain Filter chain / 过滤器链
     * @throws IOException if I/O error occurs / 如果发生 I/O 错误
     * @throws ServletException if servlet error occurs / 如果发生 servlet 错误
     * @description
     * Checks if the request rate is within limits. Returns 429 (Too Many Requests)
     * if rate limit is exceeded. Uses stricter limits for authentication endpoints.
     * 
     * 检查请求速率是否在限制内。如果超过速率限制，返回 429（请求过多）。
     * 对认证端点使用更严格的限制。
     */
    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, 
                        jakarta.servlet.ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String path = httpRequest.getRequestURI();
        Bucket bucket;
        
        // Use stricter rate limiting for authentication endpoints
        // 对认证端点使用更严格的速率限制
        if (path.startsWith("/api/auth/")) {
            bucket = authRateLimitBucket;
        } else {
            bucket = apiRateLimitBucket;
        }
        
        // Check if request is allowed (consume 1 token from bucket)
        // 检查是否允许请求（从桶中消耗 1 个令牌）
        if (!bucket.tryConsume(1)) {
            // Rate limit exceeded - Return 429 Too Many Requests
            // 超过速率限制 - 返回 429 请求过多
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write("{\"error\":\"Rate limit exceeded. Please try again later.\"}");
            httpResponse.getWriter().flush();
            return; // Stop processing - Don't continue filter chain
                   // 停止处理 - 不继续过滤器链
        }
        
        // Request allowed - Continue filter chain
        // 允许请求 - 继续过滤器链
        chain.doFilter(request, response);
    }
}

