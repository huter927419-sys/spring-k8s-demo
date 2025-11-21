<!--
  Login.vue - User Login Component
  用户登录组件 - 用户登录组件
  
  This component provides a user login form with email and password authentication.
  It includes modern UI design with animations and form validation.
  
  该组件提供使用邮箱和密码认证的用户登录表单。
  它包括现代化的 UI 设计和动画以及表单验证。
-->
<template>
  <div class="login-container">
    <!-- Background with animated shapes / 带有动画形状的背景 -->
    <div class="login-background">
      <div class="background-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>
    <!-- Login Form Content / 登录表单内容 -->
    <div class="login-content">
      <div class="login-card-wrapper">
        <!-- Header Section / 头部区域 -->
        <div class="login-header">
          <div class="logo-container">
            <el-icon :size="48" class="logo-icon"><UserFilled /></el-icon>
          </div>
          <h1 class="login-title">欢迎回来</h1>
          <p class="login-subtitle">登录您的账户以继续</p>
        </div>
        <!-- Login Form Card / 登录表单卡片 -->
        <el-card class="login-card" shadow="hover">
          <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-position="top" class="login-form">
            <el-form-item label="邮箱地址" prop="email">
              <el-input 
                v-model="loginForm.email" 
                placeholder="请输入邮箱地址" 
                size="large"
                :prefix-icon="Message"
                clearable
              />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input 
                v-model="loginForm.password" 
                type="password" 
                placeholder="请输入密码" 
                size="large"
                :prefix-icon="Lock"
                show-password
                clearable
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            <el-form-item>
              <el-button 
                type="primary" 
                @click="handleLogin" 
                :loading="loading" 
                size="large"
                class="login-button"
              >
                <span v-if="!loading">立即登录</span>
                <span v-else>登录中...</span>
              </el-button>
            </el-form-item>
            <div class="login-footer">
              <el-link type="primary" :underline="false" @click="$router.push('/register')" class="register-link">
                <el-icon><User /></el-icon>
                <span>还没有账号？立即注册</span>
              </el-link>
            </div>
          </el-form>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * Login.vue - User Login Component
 * 用户登录组件
 * 
 * @description
 * This component handles user authentication with email and password.
 * It validates user credentials and manages login state.
 * 
 * 该组件处理使用邮箱和密码的用户认证。
 * 它验证用户凭据并管理登录状态。
 */

import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../utils/auth'
import { ElMessage } from 'element-plus'
import { Message, Lock, User, UserFilled } from '@element-plus/icons-vue'

// Router instance for navigation / 用于导航的路由实例
const router = useRouter()

// Authentication store for user login / 用于用户登录的认证存储
const authStore = useAuthStore()

// Form reference for validation / 用于验证的表单引用
const loginFormRef = ref(null)

// Loading state / 加载状态
const loading = ref(false)

// Login form data / 登录表单数据
const loginForm = reactive({
  email: '',
  password: ''
})

// Form validation rules / 表单验证规则
const rules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

/**
 * Handle user login
 * 处理用户登录
 * 
 * @async
 * @function handleLogin
 * @description
 * Validates the login form and calls the authentication service
 * to authenticate the user. On success, redirects to the home page.
 * 
 * 验证登录表单并调用认证服务以认证用户。
 * 成功后，重定向到首页。
 */
const handleLogin = async () => {
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await authStore.login(loginForm.email, loginForm.password)
        ElMessage.success('登录成功，欢迎回来！')
        router.push('/')
      } catch (error) {
        ElMessage.error(error || '登录失败，请检查您的凭据')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  overflow: hidden;
}

.background-shapes {
  position: relative;
  width: 100%;
  height: 100%;
}

.shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 20s infinite ease-in-out;
}

.shape-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.shape-2 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  right: -50px;
  animation-delay: 5s;
}

.shape-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  right: 10%;
  animation-delay: 10s;
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  33% {
    transform: translate(30px, -30px) rotate(120deg);
  }
  66% {
    transform: translate(-20px, 20px) rotate(240deg);
  }
}

.login-content {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 450px;
  padding: 20px;
}

.login-card-wrapper {
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
  color: white;
}

.logo-container {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  margin-bottom: 20px;
  backdrop-filter: blur(10px);
  border: 2px solid rgba(255, 255, 255, 0.3);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.4);
  }
  50% {
    transform: scale(1.05);
    box-shadow: 0 0 0 10px rgba(255, 255, 255, 0);
  }
}

.logo-icon {
  color: white;
}

.login-title {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 10px 0;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.login-subtitle {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border: none;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  overflow: hidden;
}

.login-card :deep(.el-card__body) {
  padding: 40px;
}

.login-form {
  margin-top: 10px;
}

.login-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  font-size: 14px;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.login-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.5);
}

.login-button {
  width: 100%;
  height: 50px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
}

.login-button:active {
  transform: translateY(0);
}

.login-footer {
  text-align: center;
  margin-top: 20px;
}

.register-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #667eea;
  transition: all 0.3s ease;
}

.register-link:hover {
  color: #764ba2;
  transform: translateX(2px);
}

@media (max-width: 768px) {
  .login-content {
    max-width: 100%;
    padding: 15px;
  }
  
  .login-card :deep(.el-card__body) {
    padding: 30px 20px;
  }
  
  .login-title {
    font-size: 28px;
  }
}
</style>

