package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringK8sDemoApplication - Main Spring Boot Application Class
 * Spring Boot 主应用程序类
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This is the main entry point of the Spring Boot application.
 * It initializes the Spring application context and starts the embedded server.
 * 
 * 这是 Spring Boot 应用程序的主入口点。
 * 它初始化 Spring 应用程序上下文并启动嵌入式服务器。
 */
@SpringBootApplication
public class SpringK8sDemoApplication {

    /**
     * Main method - Application entry point
     * 主方法 - 应用程序入口点
     * 
     * @param args Command line arguments / 命令行参数
     * @description
     * Launches the Spring Boot application.
     * 启动 Spring Boot 应用程序。
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringK8sDemoApplication.class, args);
    }
}

