# Spring Boot K8s Demo - 前端项目

Vue 3 + Element Plus 前端应用，实现用户管理系统的完整功能。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vue Router** - 官方路由管理器
- **Element Plus** - Vue 3 UI 组件库
- **Axios** - HTTP 客户端
- **Vite** - 下一代前端构建工具

## 功能特性

✅ JWT 认证（登录/注册/退出）  
✅ 用户管理 CRUD 操作  
✅ Token 自动管理  
✅ 路由守卫  
✅ 响应式设计  

## 安装和运行

### 安装依赖

```bash
cd frontend
npm install
```

### 开发模式

```bash
npm run dev
```

应用将在 `http://localhost:3000` 启动

### 构建生产版本

```bash
npm run build
```

## 配置

在项目根目录创建 `.env` 文件：

```env
VITE_API_BASE_URL=http://your-backend-url:30080/api
```

## 项目结构

```
frontend/
├── src/
│   ├── api/           # API 接口
│   ├── components/    # 组件
│   ├── router/        # 路由配置
│   ├── utils/         # 工具函数（认证等）
│   ├── views/         # 页面视图
│   ├── App.vue        # 根组件
│   └── main.js        # 入口文件
├── public/            # 静态资源
├── index.html        # HTML 模板
├── vite.config.js    # Vite 配置
└── package.json      # 项目配置
```

## API 端点

- `POST /api/auth/login` - 登录
- `POST /api/auth/register` - 注册
- `POST /api/auth/logout` - 退出
- `GET /api/users` - 获取用户列表
- `GET /api/users/{id}` - 获取单个用户
- `POST /api/users` - 创建用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户

## 使用说明

1. 访问应用后，首先需要注册或登录
2. 登录成功后，可以查看和管理用户列表
3. 支持添加、编辑、删除用户操作
4. Token 会自动保存在 localStorage 中
5. 退出登录会清除所有认证信息

