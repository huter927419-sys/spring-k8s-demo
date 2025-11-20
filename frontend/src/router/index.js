/**
 * router/index.js - Vue Router Configuration
 * 路由配置 - Vue 路由配置
 * 
 * @description
 * This module configures Vue Router with route definitions and navigation guards.
 * It handles authentication-based route protection and redirects.
 * 
 * 该模块配置 Vue Router，包括路由定义和导航守卫。
 * 它处理基于认证的路由保护和重定向。
 */

import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../utils/auth'

/**
 * Route Definitions
 * 路由定义
 * 
 * @type {Array<Object>}
 * @description
 * Defines all application routes with their components and authentication requirements.
 * 定义所有应用程序路由及其组件和认证要求。
 */
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false } // Public route / 公共路由
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { requiresAuth: false } // Public route / 公共路由
  },
  {
    path: '/',
    name: 'UserList',
    component: () => import('../views/UserList.vue'),
    meta: { requiresAuth: true } // Protected route / 受保护的路由
  },
  {
    path: '/users',
    name: 'Users',
    component: () => import('../views/UserList.vue'),
    meta: { requiresAuth: true } // Protected route / 受保护的路由
  }
]

/**
 * Create Vue Router instance
 * 创建 Vue Router 实例
 */
const router = createRouter({
  history: createWebHistory(), // Use HTML5 history mode / 使用 HTML5 历史模式
  routes
})

/**
 * Navigation Guard - Authentication Check
 * 导航守卫 - 认证检查
 * 
 * @description
 * Intercepts route navigation to check authentication status:
 * - Redirects to login if accessing protected route without authentication
 * - Redirects to home if accessing login/register while authenticated
 * 
 * 拦截路由导航以检查认证状态：
 * - 如果未认证访问受保护路由，则重定向到登录页
 * - 如果已认证访问登录/注册页，则重定向到首页
 */
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const requiresAuth = to.meta.requiresAuth

  // Redirect to login if accessing protected route without authentication
  // 如果未认证访问受保护路由，则重定向到登录页
  if (requiresAuth && !authStore.isAuthenticated()) {
    next('/login')
  } 
  // Redirect to home if accessing login/register while authenticated
  // 如果已认证访问登录/注册页，则重定向到首页
  else if (!requiresAuth && authStore.isAuthenticated() && (to.path === '/login' || to.path === '/register')) {
    next('/')
  } 
  // Allow navigation / 允许导航
  else {
    next()
  }
})

export default router

