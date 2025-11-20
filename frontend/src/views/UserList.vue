<!--
  UserList.vue - User Management Component
  用户列表组件 - 用户管理组件
  
  This component displays a list of users with CRUD operations.
  It provides functionality to view, create, update, and delete users.
  
  该组件显示用户列表并提供 CRUD 操作。
  它提供查看、创建、更新和删除用户的功能。
-->
<template>
  <div class="user-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户列表</span>
          <el-button type="primary" @click="handleAdd">添加用户</el-button>
        </div>
      </template>

      <!-- User Table / 用户表格 -->
      <el-table :data="userList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机" />
        <el-table-column prop="role" label="角色" />
        <el-table-column prop="createdAt" label="创建时间" :formatter="formatDate" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Add/Edit Dialog / 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="userForm" :rules="rules" ref="userFormRef" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" :disabled="!!userForm.id" />
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="userForm.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * UserList.vue - User Management Component
 * 用户列表组件 - 用户管理组件
 * 
 * @description
 * This component provides a user management interface with table view,
 * create, update, and delete functionality.
 * 
 * 该组件提供用户管理界面，包括表格视图、
 * 创建、更新和删除功能。
 */

import { ref, reactive, onMounted } from 'vue'
import { getUserList, createUser, updateUser, deleteUser } from '../api/user'
import { ElMessage, ElMessageBox } from 'element-plus'

// Reactive state / 响应式状态
const loading = ref(false)              // Loading state for table / 表格加载状态
const submitting = ref(false)           // Loading state for form submission / 表单提交加载状态
const dialogVisible = ref(false)        // Dialog visibility / 对话框可见性
const dialogTitle = ref('添加用户')      // Dialog title / 对话框标题
const userFormRef = ref(null)           // Form reference for validation / 用于验证的表单引用
const userList = ref([])                // List of users / 用户列表

// User form data / 用户表单数据
const userForm = reactive({
  id: null,
  name: '',
  email: '',
  phone: ''
})

// Form validation rules / 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 50, message: '姓名长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

/**
 * Fetch users from API
 * 从 API 获取用户列表
 * 
 * @async
 * @function fetchUsers
 * @description
 * Retrieves the list of users from the backend API.
 * 从后端 API 检索用户列表。
 */
const fetchUsers = async () => {
  loading.value = true
  try {
    const response = await getUserList()
    if (response.data.success) {
      userList.value = response.data.data || []
    }
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * Handle add user button click
 * 处理添加用户按钮点击
 * 
 * @function handleAdd
 * @description
 * Opens the dialog in "add" mode with empty form.
 * 以"添加"模式打开对话框，表单为空。
 */
const handleAdd = () => {
  dialogTitle.value = '添加用户'
  Object.assign(userForm, {
    id: null,
    name: '',
    email: '',
    phone: ''
  })
  dialogVisible.value = true
}

/**
 * Handle edit user button click
 * 处理编辑用户按钮点击
 * 
 * @function handleEdit
 * @param {Object} row - User row data / 用户行数据
 * @description
 * Opens the dialog in "edit" mode with user data pre-filled.
 * 以"编辑"模式打开对话框，预填充用户数据。
 */
const handleEdit = (row) => {
  dialogTitle.value = '编辑用户'
  Object.assign(userForm, {
    id: row.id,
    name: row.name,
    email: row.email,
    phone: row.phone || ''
  })
  dialogVisible.value = true
}

/**
 * Handle form submission (create or update)
 * 处理表单提交（创建或更新）
 * 
 * @async
 * @function handleSubmit
 * @description
 * Validates the form and either creates a new user or updates an existing one.
 * 验证表单并创建新用户或更新现有用户。
 */
const handleSubmit = async () => {
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (userForm.id) {
          // Update existing user / 更新现有用户
          await updateUser(userForm.id, userForm)
          ElMessage.success('更新成功')
        } else {
          // Create new user / 创建新用户
          await createUser(userForm)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        fetchUsers() // Refresh user list / 刷新用户列表
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '操作失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

/**
 * Handle delete user button click
 * 处理删除用户按钮点击
 * 
 * @async
 * @function handleDelete
 * @param {Object} row - User row data / 用户行数据
 * @description
 * Shows confirmation dialog and deletes the user if confirmed.
 * 显示确认对话框，如果确认则删除用户。
 */
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    fetchUsers() // Refresh user list / 刷新用户列表
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

/**
 * Format date for display
 * 格式化日期用于显示
 * 
 * @function formatDate
 * @param {Object} row - Table row data / 表格行数据
 * @param {Object} column - Table column / 表格列
 * @param {string} cellValue - Cell value (date string) / 单元格值（日期字符串）
 * @returns {string} Formatted date string / 格式化的日期字符串
 */
const formatDate = (row, column, cellValue) => {
  if (!cellValue) return ''
  return new Date(cellValue).toLocaleString('zh-CN')
}

// Fetch users when component is mounted / 组件挂载时获取用户列表
onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.user-list-container {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

