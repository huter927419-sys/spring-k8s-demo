# Kubernetes 中 Nginx 的使用说明

## 问题：K8s 中还需要 Nginx 吗？

### 简短回答
**前端容器内：需要**（提供静态文件服务）  
**集群入口：可选**（可用 Kubernetes Ingress 替代）

---

## 详细分析

### 1. 前端容器内的 Nginx（必需）

**作用：**
- ✅ 提供静态文件服务（Vue 构建后的 HTML/CSS/JS 文件）
- ✅ 处理 SPA 路由（Vue Router 的 history 模式需要）
- ✅ 静态资源压缩和缓存优化

**为什么需要：**
Kubernetes 本身**不提供静态文件服务能力**，容器内必须有一个 Web 服务器来：
- 响应 HTTP 请求
- 返回静态文件
- 处理前端路由（`/` 路径返回 `index.html`）

**替代方案：**
- Caddy（更轻量）
- Apache
- 但 Nginx 是最常用和高效的选择

---

### 2. API 代理（可选）

**当前方案：** 前端容器内的 Nginx 代理 `/api` 到后端

**Kubernetes 原生方案：** 前端直接通过 Service 名称访问后端

#### 方案 A：Nginx 代理（当前）
```nginx
location /api {
    proxy_pass http://spring-k8s-demo-service:80;
}
```

**优点：**
- 统一入口，前端只需访问自己的域名
- 可以统一处理 CORS、认证等

**缺点：**
- 增加一层代理，略微增加延迟
- 需要维护 Nginx 配置

#### 方案 B：直接访问 Service（推荐）
前端直接调用后端 Service：
```javascript
// 前端代码
const api = axios.create({
  baseURL: 'http://spring-k8s-demo-service:80/api'  // 通过 Service 名称
})
```

**优点：**
- 更符合 Kubernetes 服务发现理念
- 减少代理层，性能更好
- 配置更简单

**缺点：**
- 需要处理 CORS（如果前后端不同域名）
- 前端需要知道后端 Service 名称

---

### 3. 集群入口（可选）

**传统方案：** 在集群外部署 Nginx 作为反向代理

**Kubernetes 方案：** 使用 **Ingress Controller**

#### Ingress Controller 方案（推荐）

```yaml
# 安装 Nginx Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml

# 使用 Ingress 资源
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: frontend-ingress
spec:
  rules:
  - host: app.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: frontend-service
            port:
              number: 80
      - path: /api
        pathType: Prefix
        backend:
          service:
            name: spring-k8s-demo-service
            port:
              number: 80
```

**优点：**
- 云原生方案，统一管理入口
- 支持 SSL/TLS 终止
- 支持基于路径/域名的路由
- 支持负载均衡和健康检查

---

## 推荐架构方案

### 方案 1：简化版（当前方案）
```
用户 → NodePort (30000) → 前端 Pod (Nginx) → 后端 Service
```
- 前端容器：Nginx（必需，提供静态文件）
- API 代理：前端 Nginx 代理
- 入口：NodePort

**适用场景：** 开发/测试环境，简单部署

---

### 方案 2：云原生版（推荐生产环境）
```
用户 → Ingress Controller → 前端 Service → 前端 Pod (Nginx)
                             后端 Service → 后端 Pod
```
- 前端容器：Nginx（必需，提供静态文件）
- API 访问：前端直接调用后端 Service
- 入口：Ingress Controller

**适用场景：** 生产环境，需要域名、SSL 等

---

### 方案 3：完全云原生（高级）
```
用户 → Ingress Controller → 前端 Service → 前端 Pod (Nginx)
                             后端 Service → 后端 Pod
                             前端直接通过 Service 访问后端
```
- 前端容器：Nginx（仅提供静态文件，不代理 API）
- API 访问：前端通过 Service 名称直接访问
- 入口：Ingress Controller

**适用场景：** 大型生产环境，微服务架构

---

## 总结

| 场景 | 是否需要 Nginx | 说明 |
|------|---------------|------|
| 前端容器内 | ✅ **必需** | 提供静态文件服务，K8s 无法替代 |
| API 代理 | ⚠️ **可选** | 可用 Service 直接访问替代 |
| 集群入口 | ⚠️ **可选** | 可用 Ingress Controller 替代 |

**最佳实践：**
1. 前端容器内保留 Nginx（提供静态文件）
2. 移除前端 Nginx 的 API 代理，前端直接访问后端 Service
3. 使用 Ingress Controller 作为集群入口（生产环境）

---

## 文件说明

- `nginx.conf` - 包含 API 代理的完整配置（当前方案）
- `nginx-simple.conf` - 仅提供静态文件服务（云原生方案）
- `deployment.yaml` - 使用完整 Nginx 配置
- `deployment-simple.yaml` - 使用简化 Nginx 配置
- `ingress.yaml` - Ingress 配置示例（需要安装 Ingress Controller）

