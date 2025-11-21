<!--
  Register.vue - User Registration Component
  用户注册组件 - 用户注册组件
  
  This component provides a user registration form with validation.
  It includes modern UI design with animations and form validation.
  
  该组件提供带验证的用户注册表单。
  它包括现代化的 UI 设计和动画以及表单验证。
-->
<template>
  <div class="register-container">
    <!-- Background with animated shapes / 带有动画形状的背景 -->
    <div class="register-background">
      <div class="background-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>
    <!-- Registration Form Content / 注册表单内容 -->
    <div class="register-content">
      <div class="register-card-wrapper">
        <!-- Header Section / 头部区域 -->
        <div class="register-header">
          <div class="logo-container">
            <el-icon :size="48" class="logo-icon"><UserFilled /></el-icon>
          </div>
          <h1 class="register-title">创建账户</h1>
          <p class="register-subtitle">填写信息以创建新账户</p>
        </div>
        <!-- Registration Form Card / 注册表单卡片 -->
        <el-card class="register-card" shadow="hover">
          <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-position="top" class="register-form">
            <el-form-item label="姓名" prop="name">
              <el-input 
                v-model="registerForm.name" 
                placeholder="请输入您的姓名" 
                size="large"
                :prefix-icon="User"
                clearable
              />
            </el-form-item>
            <el-form-item label="邮箱地址" prop="email">
              <el-input 
                v-model="registerForm.email" 
                placeholder="请输入邮箱地址" 
                size="large"
                :prefix-icon="Message"
                clearable
              />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input 
                v-model="registerForm.password" 
                type="password" 
                placeholder="请输入密码（至少6位）" 
                size="large"
                :prefix-icon="Lock"
                show-password
                clearable
              />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input 
                v-model="registerForm.phone" 
                placeholder="请输入手机号（可选）" 
                size="large"
                :prefix-icon="Phone"
                clearable
              />
            </el-form-item>
            <el-form-item>
              <el-button 
                type="primary" 
                @click="handleRegister" 
                :loading="loading" 
                size="large"
                class="register-button"
              >
                <span v-if="!loading">立即注册</span>
                <span v-else>注册中...</span>
              </el-button>
            </el-form-item>
            <div class="register-footer">
              <el-link type="primary" :underline="false" @click="$router.push('/login')" class="login-link">
                <el-icon><User /></el-icon>
                <span>已有账号？立即登录</span>
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
 * Register.vue - User Registration Component
 * 用户注册组件
 * 
 * @description
 * This component handles user registration with form validation.
 * It validates user input and calls the authentication service to create a new account.
 * 
 * 该组件处理带表单验证的用户注册。
 * 它验证用户输入并调用认证服务以创建新账户。
 */

import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../utils/auth'
import { ElMessage } from 'element-plus'
import { Message, Lock, User, UserFilled, Phone } from '@element-plus/icons-vue'

// Router instance for navigation / 用于导航的路由实例
const router = useRouter()

// Authentication store for user registration / 用于用户注册的认证存储
const authStore = useAuthStore()

// Form reference for validation / 用于验证的表单引用
const registerFormRef = ref(null)

// Loading state / 加载状态
const loading = ref(false)

// Registration form data / 注册表单数据
const registerForm = reactive({
  name: '',
  email: '',
  password: '',
  phone: ''
})

// Form validation rules / 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入您的姓名', trigger: 'blur' },
    { min: 2, max: 50, message: '姓名长度在 2 到 50 个字符', trigger: 'blur' }
  ],
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
 * Handle user registration
 * 处理用户注册
 * 
 * @async
 * @function handleRegister
 * @description
 * Validates the registration form and calls the authentication service
 * to register a new user. On success, redirects to the home page.
 * 
 * 验证注册表单并调用认证服务以注册新用户。
 * 成功后，重定向到首页。
 */
const handleRegister = async () => {
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await authStore.register(registerForm)
        ElMessage.success('注册成功，欢迎加入！')
        router.push('/')
      } catch (error) {
        ElMessage.error(error || '注册失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.register-container {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-background {
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

.register-content {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 500px;
  padding: 20px;
}

.register-card-wrapper {
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

.register-header {
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

.register-title {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 10px 0;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.register-subtitle {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.register-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border: none;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  overflow: hidden;
}

.register-card :deep(.el-card__body) {
  padding: 40px;
}

.register-form {
  margin-top: 10px;
}

.register-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  font-size: 14px;
}

.register-form :deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.register-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.register-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.5);
}

.register-button {
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

.register-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
}

.register-button:active {
  transform: translateY(0);
}

.register-footer {
  text-align: center;
  margin-top: 20px;
}

.login-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #667eea;
  transition: all 0.3s ease;
}

.login-link:hover {
  color: #764ba2;
  transform: translateX(2px);
}

@media (max-width: 768px) {
  .register-content {
    max-width: 100%;
    padding: 15px;
  }
  
  .register-card :deep(.el-card__body) {
    padding: 30px 20px;
  }
  
  .register-title {
    font-size: 28px;
  }
}
</style>

