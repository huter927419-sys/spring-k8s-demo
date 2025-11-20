/**
 * api/index.js - Axios API Configuration
 * API 配置文件 - Axios API 配置
 * 
 * @description
 * This module configures the Axios HTTP client with base URL detection,
 * request/response interceptors for authentication, and error handling.
 * 
 * 该模块配置 Axios HTTP 客户端，包括基础 URL 检测、
 * 用于认证的请求/响应拦截器和错误处理。
 */

import axios from 'axios'

/**
 * Get API base URL based on environment
 * 根据环境获取 API 基础 URL
 * 
 * @function getBaseURL
 * @returns {string} API base URL / API 基础 URL
 * @description
 * Automatically detects the environment:
 * - Production (K8s): Uses relative path '/api' (proxied by nginx)
 * - Development: Uses 'http://localhost:8080/api'
 * - Custom: Uses VITE_API_BASE_URL environment variable
 * 
 * 自动检测环境：
 * - 生产环境（K8s）：使用相对路径 '/api'（由 nginx 代理）
 * - 开发环境：使用 'http://localhost:8080/api'
 * - 自定义：使用 VITE_API_BASE_URL 环境变量
 */
const getBaseURL = () => {
  // Custom base URL from environment variable / 从环境变量获取自定义基础 URL
  if (import.meta.env.VITE_API_BASE_URL) {
    return import.meta.env.VITE_API_BASE_URL
  }
  // Production environment (K8s): Use relative path proxied by nginx
  // 生产环境（K8s）：使用由 nginx 代理的相对路径
  if (window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1') {
    return '/api'
  }
  // Development environment / 开发环境
  return 'http://localhost:8080/api'
}

/**
 * Axios instance with default configuration
 * 带有默认配置的 Axios 实例
 */
const api = axios.create({
  baseURL: getBaseURL(),
  timeout: 10000, // Request timeout: 10 seconds / 请求超时：10 秒
  headers: {
    'Content-Type': 'application/json'
  }
})

/**
 * Request Interceptor
 * 请求拦截器
 * 
 * @description
 * Automatically adds JWT token to request headers if available.
 * 如果可用，自动将 JWT token 添加到请求头。
 */
api.interceptors.request.use(
  config => {
    // Get token from localStorage / 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      // Add Bearer token to Authorization header / 将 Bearer token 添加到 Authorization 头
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

/**
 * Response Interceptor
 * 响应拦截器
 * 
 * @description
 * Handles authentication errors (401) by clearing tokens and redirecting to login.
 * 通过清除 token 并重定向到登录页面来处理认证错误（401）。
 */
api.interceptors.response.use(
  response => response,
  error => {
    // Handle 401 Unauthorized: Clear tokens and redirect to login
    // 处理 401 未授权：清除 token 并重定向到登录页面
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default api

