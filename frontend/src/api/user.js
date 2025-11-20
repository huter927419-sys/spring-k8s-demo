/**
 * api/user.js - User API Service
 * 用户 API 服务
 * 
 * @description
 * This module provides API functions for user management operations.
 * All functions use the configured axios instance with authentication headers.
 * 
 * 该模块提供用于用户管理操作的 API 函数。
 * 所有函数都使用配置的 axios 实例和认证头。
 */

import api from './index'

/**
 * Get all users
 * 获取所有用户
 * 
 * @function getUserList
 * @returns {Promise} Axios response with user list / 包含用户列表的 Axios 响应
 */
export const getUserList = () => {
  return api.get('/users')
}

/**
 * Get user by ID
 * 根据 ID 获取用户
 * 
 * @function getUserById
 * @param {number|string} id - User ID / 用户 ID
 * @returns {Promise} Axios response with user data / 包含用户数据的 Axios 响应
 */
export const getUserById = (id) => {
  return api.get(`/users/${id}`)
}

/**
 * Create a new user
 * 创建新用户
 * 
 * @function createUser
 * @param {Object} userData - User data to create / 要创建的用户数据
 * @returns {Promise} Axios response with created user / 包含创建用户的 Axios 响应
 */
export const createUser = (userData) => {
  return api.post('/users', userData)
}

/**
 * Update an existing user
 * 更新现有用户
 * 
 * @function updateUser
 * @param {number|string} id - User ID to update / 要更新的用户 ID
 * @param {Object} userData - Updated user data / 更新的用户数据
 * @returns {Promise} Axios response with updated user / 包含更新用户的 Axios 响应
 */
export const updateUser = (id, userData) => {
  return api.put(`/users/${id}`, userData)
}

/**
 * Delete a user by ID
 * 根据 ID 删除用户
 * 
 * @function deleteUser
 * @param {number|string} id - User ID to delete / 要删除的用户 ID
 * @returns {Promise} Axios response / Axios 响应
 */
export const deleteUser = (id) => {
  return api.delete(`/users/${id}`)
}

