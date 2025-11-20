#!/bin/bash

echo "=== 部署完整的 Spring Boot + Vue + JWT + Redis + MySQL 到 Kubernetes ==="
echo ""

# 检查 kubectl
if ! command -v kubectl &> /dev/null; then
    echo "错误: kubectl 未安装"
    exit 1
fi

# 部署 MySQL
echo "1. 部署 MySQL..."
kubectl apply -f spring-k8s-demo/k8s/mysql-pv.yaml
kubectl apply -f spring-k8s-demo/k8s/mysql-pvc.yaml
kubectl apply -f spring-k8s-demo/k8s/mysql-secret.yaml
kubectl apply -f spring-k8s-demo/k8s/mysql-deployment.yaml
kubectl apply -f spring-k8s-demo/k8s/mysql-service.yaml

# 部署 Redis
echo "2. 部署 Redis..."
kubectl apply -f spring-k8s-demo/k8s/redis-secret.yaml
kubectl apply -f spring-k8s-demo/k8s/redis-deployment.yaml
kubectl apply -f spring-k8s-demo/k8s/redis-service.yaml

# 等待 MySQL 和 Redis 就绪
echo "3. 等待 MySQL 和 Redis 启动..."
kubectl wait --for=condition=ready pod -l app=mysql --timeout=300s || true
kubectl wait --for=condition=ready pod -l app=redis --timeout=300s || true

# 部署后端应用
echo "4. 部署 Spring Boot 后端..."
kubectl apply -f spring-k8s-demo/k8s/deployment.yaml
kubectl apply -f spring-k8s-demo/k8s/service.yaml

# 部署前端应用
echo "5. 部署 Vue 前端..."
kubectl apply -f frontend/k8s/deployment.yaml
kubectl apply -f frontend/k8s/service.yaml

echo ""
echo "=== 部署完成 ==="
echo ""
echo "查看状态:"
echo "  kubectl get pods"
echo "  kubectl get svc"
echo ""
NODE_IP=$(kubectl get nodes -o jsonpath='{.items[0].status.addresses[?(@.type=="InternalIP")].address}' 2>/dev/null || echo "<NODE_IP>")
echo "访问地址:"
echo "  前端: http://$NODE_IP:30000"
echo "  后端: http://$NODE_IP:30080/api"
