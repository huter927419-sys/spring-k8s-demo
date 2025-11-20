# Spring Boot Kubernetes 完整示例

这是一个完整的、生产就绪的 Spring Boot 应用示例，展示了如何在 Kubernetes 上部署包含数据库的完整应用。

## 技术栈

- **Spring Boot**: 3.3.5 (最新稳定版)
- **Java**: 17 (OpenJDK)
- **数据库**: MySQL 8.0
- **ORM**: Spring Data JPA / Hibernate
- **容器**: Docker
- **编排**: Kubernetes 1.31.14

## 功能特性

✅ 完整的 CRUD API（用户管理）  
✅ MySQL 数据库集成  
✅ JPA/Hibernate ORM  
✅ 数据验证（Bean Validation）  
✅ 健康检查（Actuator）  
✅ 持久化存储（PVC）  
✅ 配置管理（Secret/ConfigMap）  
✅ 多副本部署（3个副本）  
✅ 资源限制和健康探针  

## 项目结构

```
spring-k8s-demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── entity/User.java          # 用户实体类
│   │   │   ├── repository/UserRepository.java  # 数据访问层
│   │   │   ├── service/UserService.java  # 业务逻辑层
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java  # REST API 控制器
│   │   │   │   └── HelloController.java # 示例控制器
│   │   │   └── SpringK8sDemoApplication.java
│   │   └── resources/
│   │       ├── application.properties    # 应用配置
│   │       ├── schema.sql                # 数据库 schema
│   │       └── data.sql                  # 初始数据
├── k8s/
│   ├── deployment.yaml          # Spring Boot 应用部署
│   ├── service.yaml             # 应用服务
│   ├── mysql-deployment.yaml    # MySQL 数据库部署
│   ├── mysql-service.yaml       # MySQL 服务
│   ├── mysql-pvc.yaml           # 持久化存储
│   └── mysql-secret.yaml        # 数据库密码（Secret）
├── Dockerfile                   # Docker 镜像构建
├── pom.xml                      # Maven 配置
└── README.md                    # 本文档
```

## API 端点

### 用户管理 API

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/users` | 获取所有用户 |
| GET | `/api/users/{id}` | 获取单个用户 |
| POST | `/api/users` | 创建新用户 |
| PUT | `/api/users/{id}` | 更新用户 |
| DELETE | `/api/users/{id}` | 删除用户 |

### 其他端点

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/hello` | Hello 消息 |
| GET | `/api/health` | 健康检查 |
| GET | `/api/info` | 应用信息 |
| GET | `/actuator/health` | Spring Actuator 健康检查 |

## 部署步骤

### 1. 构建应用

```bash
cd spring-k8s-demo
mvn clean package -DskipTests
```

### 2. 构建 Docker 镜像

```bash
docker build -t spring-k8s-demo:1.0.0 .
```

### 3. 导入镜像到 containerd（如果使用 containerd）

```bash
docker save spring-k8s-demo:1.0.0 | ctr -n k8s.io images import -
```

### 4. 部署到 Kubernetes

```bash
# 部署 MySQL
kubectl apply -f k8s/mysql-pvc.yaml
kubectl apply -f k8s/mysql-secret.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/mysql-service.yaml

# 等待 MySQL 启动
kubectl wait --for=condition=ready pod -l app=mysql --timeout=300s

# 部署 Spring Boot 应用
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

### 5. 验证部署

```bash
# 查看 Pod 状态
kubectl get pods

# 查看服务
kubectl get svc

# 查看日志
kubectl logs -l app=spring-k8s-demo
```

## 使用示例

### 创建用户

```bash
curl -X POST http://<NODE_IP>:30080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三",
    "email": "zhangsan@example.com",
    "phone": "13800138000"
  }'
```

### 获取所有用户

```bash
curl http://<NODE_IP>:30080/api/users
```

### 获取单个用户

```bash
curl http://<NODE_IP>:30080/api/users/1
```

### 更新用户

```bash
curl -X PUT http://<NODE_IP>:30080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三（已更新）",
    "email": "zhangsan@example.com",
    "phone": "13900139000"
  }'
```

### 删除用户

```bash
curl -X DELETE http://<NODE_IP>:30080/api/users/1
```

## 配置说明

### 数据库配置

数据库配置通过环境变量和 Secret 管理：

- `DB_HOST`: MySQL 服务地址（默认: mysql-service）
- `DB_PORT`: MySQL 端口（默认: 3306）
- `DB_NAME`: 数据库名称（从 Secret 读取）
- `DB_USER`: 数据库用户（从 Secret 读取）
- `DB_PASSWORD`: 数据库密码（从 Secret 读取）

### 资源限制

**Spring Boot 应用:**
- 请求: 256Mi 内存, 200m CPU
- 限制: 512Mi 内存, 500m CPU

**MySQL:**
- 请求: 512Mi 内存, 250m CPU
- 限制: 1Gi 内存, 500m CPU

## 故障排查

### 查看 Pod 状态

```bash
kubectl get pods -l app=spring-k8s-demo
kubectl describe pod <pod-name>
```

### 查看日志

```bash
# 应用日志
kubectl logs -l app=spring-k8s-demo

# MySQL 日志
kubectl logs -l app=mysql
```

### 检查数据库连接

```bash
# 进入应用 Pod
kubectl exec -it <spring-pod-name> -- sh

# 测试数据库连接（需要安装 mysql-client）
# 或查看应用日志中的数据库连接信息
```

## 扩展和优化

### 扩展副本数

```bash
kubectl scale deployment spring-k8s-demo --replicas=5
```

### 更新应用

```bash
# 重新构建镜像
docker build -t spring-k8s-demo:1.0.1 .

# 更新部署
kubectl set image deployment/spring-k8s-demo spring-k8s-demo=spring-k8s-demo:1.0.1
```

### 添加 Ingress

可以添加 Ingress 配置以提供域名访问：

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: spring-k8s-demo-ingress
spec:
  rules:
  - host: api.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: spring-k8s-demo-service
            port:
              number: 80
```

## 许可证

MIT License

