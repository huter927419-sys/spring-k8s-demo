<!--
  App.vue - Main Application Component
  主应用组件 - 应用程序的根组件
  
  This component serves as the root layout of the application.
  It includes a header with user information and logout functionality,
  and a main content area that displays routed components.
  
  该组件作为应用程序的根布局，包含用户信息头部和退出登录功能，
  以及显示路由组件的主要内容区域。
-->
<template>
  <el-container>
    <!-- Header: Displayed only when user is authenticated / 头部：仅在用户已登录时显示 -->
    <el-header v-if="isAuthenticated">
      <div class="header-content">
        <h2>用户管理系统</h2>
        <div class="user-info">
          <span>欢迎, {{ currentUser?.name }}</span>
          <el-button type="danger" @click="handleLogout">退出登录</el-button>
        </div>
      </div>
    </el-header>
    <!-- Main Content Area: Router view for displaying different pages / 主要内容区域：路由视图用于显示不同页面 -->
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
/**
 * App.vue - Main Application Component
 * 主应用组件
 * 
 * @description
 * This is the root component of the Vue application. It manages the overall layout,
 * authentication state, and provides logout functionality.
 * 
 * 这是 Vue 应用程序的根组件。它管理整体布局、认证状态，并提供退出登录功能。
 */

import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from './utils/auth'
import { ElMessage } from 'element-plus'

// Router instance for navigation / 用于导航的路由实例
const router = useRouter()

// Authentication store for managing user state / 用于管理用户状态的认证存储
const authStore = useAuthStore()

/**
 * Computed property: Check if user is authenticated
 * 计算属性：检查用户是否已认证
 * @returns {boolean} True if user is authenticated / 如果用户已认证则返回 true
 */
const isAuthenticated = computed(() => authStore.isAuthenticated)

/**
 * Computed property: Get current logged-in user information
 * 计算属性：获取当前登录用户信息
 * @returns {Object|null} Current user object or null / 当前用户对象或 null
 */
const currentUser = computed(() => authStore.user)

/**
 * Handle user logout
 * 处理用户退出登录
 * 
 * @async
 * @function handleLogout
 * @description
 * Logs out the current user, clears authentication state,
 * and redirects to the login page.
 * 
 * 退出当前用户登录，清除认证状态，并重定向到登录页面。
 */
const handleLogout = async () => {
  try {
    await authStore.logout()
    ElMessage.success('退出登录成功')
    router.push('/login')
  } catch (error) {
    ElMessage.error('退出登录失败')
  }
}
</script>

<style>
/* Global Reset / 全局重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* Application Root Styles / 应用程序根样式 */
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* Header Styles / 头部样式 */
.el-header {
  /* Purple gradient background / 紫色渐变背景 */
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  line-height: 60px;
  padding: 0 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

/* Header Content Layout / 头部内容布局 */
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* User Info Section / 用户信息区域 */
.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

/* Main Content Area / 主要内容区域 */
.el-main {
  padding: 20px;
  min-height: calc(100vh - 60px);
}
</style>

