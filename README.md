# Spring Boot + Vue.js Kubernetes Demo

A full-stack application demonstrating Spring Boot backend and Vue.js frontend deployment on Kubernetes with comprehensive features including JWT authentication, Redis caching, MySQL database, Prometheus monitoring, and Grafana dashboards.

一个全栈应用程序，演示了在 Kubernetes 上部署 Spring Boot 后端和 Vue.js 前端，包含 JWT 认证、Redis 缓存、MySQL 数据库、Prometheus 监控和 Grafana 仪表板等完整功能。

## 🚀 Features / 功能特性

### Backend / 后端
- ✅ Spring Boot 3.x with Java 17
- ✅ JWT Authentication with Redis session management
- ✅ MySQL 8.0 database with JPA/Hibernate
- ✅ Redis caching for performance optimization
- ✅ API Rate Limiting (Bucket4j)
- ✅ Spring Security with stateless authentication
- ✅ Prometheus metrics export
- ✅ Comprehensive code documentation (English & Chinese)

### Frontend / 前端
- ✅ Vue 3 with Composition API
- ✅ Element Plus UI components
- ✅ JWT token management
- ✅ Modern UI/UX design with animations
- ✅ Responsive layout

### Infrastructure / 基础设施
- ✅ Kubernetes deployment with HPA
- ✅ Traefik Ingress Controller
- ✅ Cert-Manager for automatic SSL/TLS
- ✅ Prometheus + Grafana monitoring
- ✅ MySQL and Redis with persistent storage
- ✅ Security best practices (non-root containers, security contexts)

## 📁 Project Structure / 项目结构

```
.
├── frontend/                 # Vue.js frontend application
│   ├── src/
│   │   ├── api/            # API service layer
│   │   ├── views/          # Vue components
│   │   ├── utils/          # Utility functions
│   │   └── router/         # Vue Router configuration
│   ├── k8s/                # Kubernetes manifests
│   └── Dockerfile          # Frontend container image
│
├── spring-k8s-demo/         # Spring Boot backend application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/demo/
│   │   │   │       ├── config/      # Configuration classes
│   │   │   │       ├── controller/  # REST controllers
│   │   │   │       ├── service/     # Business logic
│   │   │   │       ├── entity/      # JPA entities
│   │   │   │       ├── filter/       # Security filters
│   │   │   │       └── util/         # Utility classes
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── k8s/            # Kubernetes manifests
│   └── Dockerfile          # Backend container image
│
└── k8s/                    # Shared Kubernetes resources
    ├── traefik/            # Traefik Ingress Controller
    ├── prometheus/         # Prometheus configuration
    └── grafana/            # Grafana configuration
```

## 🛠️ Technology Stack / 技术栈

### Backend / 后端
- **Framework**: Spring Boot 3.2.x
- **Language**: Java 17
- **Database**: MySQL 8.0
- **Cache**: Redis 7.x
- **Security**: Spring Security + JWT
- **Build Tool**: Maven
- **Monitoring**: Spring Actuator + Prometheus

### Frontend / 前端
- **Framework**: Vue 3 (Composition API)
- **UI Library**: Element Plus
- **Build Tool**: Vite
- **HTTP Client**: Axios
- **Router**: Vue Router

### Infrastructure / 基础设施
- **Container Orchestration**: Kubernetes 1.28+
- **Ingress Controller**: Traefik
- **SSL/TLS**: Cert-Manager + Let's Encrypt
- **Monitoring**: Prometheus + Grafana
- **Container Runtime**: Docker / containerd

## 📦 Prerequisites / 前置要求

- Kubernetes cluster (1.28+)
- kubectl configured
- Docker or containerd
- Maven 3.8+
- Node.js 18+ and npm
- Domain name (for SSL certificates)

## 🚀 Quick Start / 快速开始

### 1. Build Docker Images / 构建 Docker 镜像

```bash
# Build backend image
cd spring-k8s-demo
mvn clean package
docker build -t spring-k8s-demo:1.0.0 .

# Build frontend image
cd ../frontend
npm install
npm run build
docker build -t spring-k8s-frontend:1.0.0 .
```

### 2. Load Images to Kubernetes / 加载镜像到 Kubernetes

```bash
# Export and import backend image
docker save spring-k8s-demo:1.0.0 | ctr -n k8s.io images import -

# Export and import frontend image
docker save spring-k8s-frontend:1.0.0 | ctr -n k8s.io images import -
```

### 3. Deploy to Kubernetes / 部署到 Kubernetes

```bash
# Deploy all resources
./deploy-all.sh

# Or deploy individually
kubectl apply -f spring-k8s-demo/k8s/
kubectl apply -f frontend/k8s/
kubectl apply -f k8s/
```

### 4. Configure Ingress / 配置 Ingress

Update `frontend/k8s/ingress-traefik.yaml` with your domain name.

## 📚 Documentation / 文档

- [Deployment Guide](./DEPLOYMENT.md) - Detailed deployment instructions
- [Optimization Report](./OPTIMIZATION_REPORT.md) - Performance optimizations
- [GitHub Setup Guide](./GITHUB_SETUP.md) - GitHub repository setup

## 🔐 Security Features / 安全特性

- JWT token-based authentication
- Password encryption with BCrypt
- API rate limiting
- Non-root container execution
- Security contexts and Pod Security Standards
- Secrets management via Kubernetes Secrets
- HTTPS/TLS encryption

## 📊 Monitoring / 监控

- **Prometheus**: Metrics collection at `/actuator/prometheus`
- **Grafana**: Accessible at `https://your-domain.com/grafana/`
- **Spring Actuator**: Health checks and metrics

## 🧪 Testing / 测试

### API Endpoints / API 端点

- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/users` - Get all users (requires authentication)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Load Testing / 负载测试

```bash
# Test API rate limiting
ab -n 1000 -c 10 https://your-domain.com/api/users
```

## 📝 Code Standards / 代码规范

All code includes comprehensive documentation with:
- English and Chinese comments
- JavaDoc for Java classes
- JSDoc-style comments for JavaScript/Vue
- Inline comments explaining complex logic

所有代码都包含完整的文档，包括：
- 中英文注释
- Java 类的 JavaDoc
- JavaScript/Vue 的 JSDoc 风格注释
- 解释复杂逻辑的内联注释

## 🤝 Contributing / 贡献

Contributions are welcome! Please ensure:
- Code follows existing style and conventions
- All new code includes documentation (English & Chinese)
- Tests pass before submitting PR

欢迎贡献！请确保：
- 代码遵循现有风格和约定
- 所有新代码包含文档（中英文）
- 提交 PR 前测试通过

## 📄 License / 许可证

This project is licensed under the MIT License.

## 👥 Authors / 作者

- Spring K8s Demo Team

## 🙏 Acknowledgments / 致谢

- Spring Boot Team
- Vue.js Team
- Kubernetes Community
- Traefik Team
- Prometheus & Grafana Teams

---

**Note**: This is a demonstration project. For production use, please review and enhance security configurations, add comprehensive tests, and follow best practices for your specific use case.

**注意**：这是一个演示项目。用于生产环境时，请审查并增强安全配置，添加全面的测试，并遵循适合您特定用例的最佳实践。

