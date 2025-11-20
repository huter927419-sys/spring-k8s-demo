/**
 * utils/auth.js - Authentication Store
 * 认证工具模块 - 认证状态管理
 * 
 * @description
 * This module provides authentication state management using Vue's reactive system.
 * It handles user login, registration, logout, and token management.
 * 
 * 该模块使用 Vue 的响应式系统提供认证状态管理。
 * 它处理用户登录、注册、退出登录和 token 管理。
 */

import { reactive } from 'vue'
import api from '../api'

/**
 * Reactive authentication state
 * 响应式认证状态
 * 
 * @type {Object}
 * @property {string|null} token - JWT authentication token / JWT 认证令牌
 * @property {Object|null} user - Current user information / 当前用户信息
 */
const authState = reactive({
  token: localStorage.getItem('token') || null,
  user: JSON.parse(localStorage.getItem('user') || 'null')
})

/**
 * Authentication Store Hook
 * 认证存储 Hook
 * 
 * @function useAuthStore
 * @returns {Object} Authentication store with methods and getters / 包含方法和 getter 的认证存储
 * @description
 * Provides authentication-related methods and state management.
 * 提供认证相关的方法和状态管理。
 */
export const useAuthStore = () => {
  /**
   * Check if user is authenticated
   * 检查用户是否已认证
   * 
   * @function isAuthenticated
   * @returns {boolean} True if user has a valid token / 如果用户有有效 token 则返回 true
   */
  const isAuthenticated = () => {
    return !!authState.token
  }

  /**
   * User login
   * 用户登录
   * 
   * @async
   * @function login
   * @param {string} email - User email address / 用户邮箱地址
   * @param {string} password - User password / 用户密码
   * @returns {Promise<Object>} Login response data / 登录响应数据
   * @throws {Error} If login fails / 如果登录失败
   * @description
   * Authenticates user with email and password, stores token and user info.
   * 使用邮箱和密码认证用户，存储 token 和用户信息。
   */
  const login = async (email, password) => {
    try {
      const response = await api.post('/auth/login', { email, password })
      if (response.data.success) {
        const { token, ...user } = response.data.data
        // Update reactive state / 更新响应式状态
        authState.token = token
        authState.user = user
        // Persist to localStorage / 持久化到 localStorage
        localStorage.setItem('token', token)
        localStorage.setItem('user', JSON.stringify(user))
        // Set default authorization header / 设置默认授权头
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`
        return response.data
      } else {
        throw new Error(response.data.message || '登录失败')
      }
    } catch (error) {
      throw error.response?.data?.message || error.message || '登录失败'
    }
  }

  /**
   * User registration
   * 用户注册
   * 
   * @async
   * @function register
   * @param {Object} userData - User registration data / 用户注册数据
   * @returns {Promise<Object>} Registration response data / 注册响应数据
   * @throws {Error} If registration fails / 如果注册失败
   * @description
   * Registers a new user and automatically logs them in.
   * 注册新用户并自动登录。
   */
  const register = async (userData) => {
    try {
      const response = await api.post('/auth/register', userData)
      if (response.data.success) {
        const { token, ...user } = response.data.data
        // Update reactive state / 更新响应式状态
        authState.token = token
        authState.user = user
        // Persist to localStorage / 持久化到 localStorage
        localStorage.setItem('token', token)
        localStorage.setItem('user', JSON.stringify(user))
        // Set default authorization header / 设置默认授权头
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`
        return response.data
      } else {
        throw new Error(response.data.message || '注册失败')
      }
    } catch (error) {
      throw error.response?.data?.message || error.message || '注册失败'
    }
  }

  /**
   * User logout
   * 用户退出登录
   * 
   * @async
   * @function logout
   * @description
   * Logs out the current user, clears authentication state and localStorage.
   * 退出当前用户登录，清除认证状态和 localStorage。
   */
  const logout = async () => {
    try {
      // Call logout endpoint if token exists / 如果 token 存在则调用退出登录端点
      if (authState.token) {
        await api.post('/auth/logout', {}, {
          headers: { Authorization: `Bearer ${authState.token}` }
        })
      }
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      // Clear state and localStorage / 清除状态和 localStorage
      authState.token = null
      authState.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      delete api.defaults.headers.common['Authorization']
    }
  }

  // Initialize: Set token in axios headers if available
  // 初始化：如果可用，在 axios 头中设置 token
  if (authState.token) {
    api.defaults.headers.common['Authorization'] = `Bearer ${authState.token}`
  }

  return {
    get token() { return authState.token },
    get user() { return authState.user },
    isAuthenticated,
    login,
    register,
    logout
  }
}

