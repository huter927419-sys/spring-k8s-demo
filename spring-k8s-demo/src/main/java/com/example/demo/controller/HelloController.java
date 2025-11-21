package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * HelloController - Health Check and Info Endpoints
 * Hello 控制器 - 健康检查和信息端点
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This controller provides simple health check and application info endpoints.
 * These endpoints are public and do not require authentication.
 * 
 * 该控制器提供简单的健康检查和应用程序信息端点。
 * 这些端点是公共的，不需要认证。
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    /**
     * Application name from configuration
     * 来自配置的应用程序名称
     */
    @Value("${spring.application.name:spring-k8s-demo}")
    private String appName;

    /**
     * Hello endpoint - Basic greeting message
     * Hello 端点 - 基本问候消息
     * 
     * @GET /api/hello
     * @return Map containing greeting message and application info / 包含问候消息和应用程序信息的 Map
     * @description
     * Returns a greeting message with application information.
     * 返回包含应用程序信息的问候消息。
     */
    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello from Spring Boot on Kubernetes!");
        response.put("timestamp", LocalDateTime.now());
        response.put("application", appName);
        response.put("version", "1.0.0");
        response.put("description", "Complete CRUD API example with MySQL database");
        return response;
    }

    /**
     * Health check endpoint
     * 健康检查端点
     * 
     * @GET /api/health
     * @return Map containing health status / 包含健康状态的 Map
     * @description
     * Simple health check endpoint. Returns UP status if service is running.
     * 简单的健康检查端点。如果服务正在运行，返回 UP 状态。
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", appName);
        return response;
    }
    
    /**
     * Application info endpoint
     * 应用程序信息端点
     * 
     * @GET /api/info
     * @return Map containing application information and available endpoints / 包含应用程序信息和可用端点的 Map
     * @description
     * Returns application metadata and list of available API endpoints.
     * 返回应用程序元数据和可用 API 端点列表。
     */
    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", appName);
        response.put("version", "1.0.0");
        response.put("description", "Spring Boot 3.3.5 with MySQL on Kubernetes");
        response.put("endpoints", Map.of(
            "users", "/api/users",
            "hello", "/api/hello",
            "health", "/api/health",
            "actuator", "/actuator/health"
        ));
        return response;
    }
}

