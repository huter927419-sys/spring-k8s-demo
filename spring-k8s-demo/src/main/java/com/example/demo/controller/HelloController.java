package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Value("${spring.application.name:spring-k8s-demo}")
    private String appName;

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

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", appName);
        return response;
    }
    
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

