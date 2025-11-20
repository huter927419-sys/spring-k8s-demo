# 完整部署指南 - Spring Boot + Vue + JWT + Redis + MySQL

## 架构概览

- **后端**: Spring Boot 3.3.5 + MySQL + Redis + JWT
- **前端**: Vue 3 + Element Plus
- **容器**: Docker
- **编排**: Kubernetes

## 部署步骤

### 1. 部署后端服务

```bash
cd spring-k8s-demo

# 部署 MySQL
kubectl apply -f k8s/mysql-pv.yaml
kubectl apply -f k8s/mysql-pvc.yaml
kubectl apply -f k8s/mysql-secret.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/mysql-service.yaml

# 部署 Redis
kubectl apply -f k8s/redis-secret.yaml
kubectl apply -f k8s/redis-deployment.yaml
kubectl apply -f k8s/redis-service.yaml

# 部署 Spring Boot 应用
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

### 2. 部署前端

前端可以通过以下方式部署：

#### 方式1: 使用 Nginx 容器部署

```bash
cd frontend
npm install
npm run build

# 创建 Dockerfile
cat > Dockerfile << 'DOCKERFILE'
FROM nginx:alpine
COPY dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
DOCKERFILE

# 创建 nginx 配置
cat > nginx.conf << 'NGINX'
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://spring-k8s-demo-service:80;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
NGINX

# 构建镜像
docker build -t spring-k8s-frontend:1.0.0 .

# 导入到 containerd
docker save spring-k8s-frontend:1.0.0 | ctr -n k8s.io images import -
```

#### 方式2: 本地开发模式

```bash
cd frontend
npm install
npm run dev
```

访问 `http://localhost:3000`

### 3. 验证部署

```bash
# 检查所有 Pod
kubectl get pods

# 检查服务
kubectl get svc

# 查看应用日志
kubectl logs -l app=spring-k8s-demo
```

## 访问地址

- **后端 API**: `http://<NODE_IP>:30080/api`
- **前端应用**: `http://localhost:3000` (开发模式)

## 功能测试

1. 访问前端应用
2. 注册新用户
3. 登录系统
4. 管理用户列表（增删改查）

## 故障排查

查看各组件日志：
- Spring Boot: `kubectl logs -l app=spring-k8s-demo`
- MySQL: `kubectl logs -l app=mysql`
- Redis: `kubectl logs -l app=redis`

## 前端 Kubernetes 部署

### 1. 构建前端 Docker 镜像

```bash
cd frontend
docker build -t spring-k8s-frontend:1.0.0 .
```

### 2. 导入镜像到 containerd

```bash
docker save spring-k8s-frontend:1.0.0 | ctr -n k8s.io images import -
```

### 3. 部署前端到 Kubernetes

```bash
kubectl apply -f frontend/k8s/deployment.yaml
kubectl apply -f frontend/k8s/service.yaml
```

### 4. 验证部署

```bash
kubectl get pods -l app=frontend
kubectl get svc frontend-service
```

## 完整访问地址

- **前端应用**: `http://<NODE_IP>:30000`
- **后端 API**: `http://<NODE_IP>:30080/api`

前端会自动代理 `/api` 请求到后端服务。
