#!/bin/bash

# GitHub Repository Creation Script
# 使用 GitHub API 创建仓库

GITHUB_USER="huter927419-sys"
REPO_NAME="spring-k8s-demo"
GITHUB_TOKEN="${GITHUB_TOKEN:-}"

if [ -z "$GITHUB_TOKEN" ]; then
    echo "错误: 需要设置 GITHUB_TOKEN 环境变量"
    echo ""
    echo "请按以下步骤获取 Token:"
    echo "1. 访问: https://github.com/settings/tokens"
    echo "2. 点击 'Generate new token' -> 'Generate new token (classic)'"
    echo "3. 设置权限: 勾选 'repo' (完整仓库权限)"
    echo "4. 生成并复制 Token"
    echo ""
    echo "然后运行:"
    echo "  export GITHUB_TOKEN='your_token_here'"
    echo "  bash create_repo.sh"
    exit 1
fi

echo "正在创建仓库: $GITHUB_USER/$REPO_NAME"
echo ""

# 创建仓库
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST \
  -H "Authorization: token $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github.v3+json" \
  https://api.github.com/user/repos \
  -d "{
    \"name\": \"$REPO_NAME\",
    \"description\": \"Spring Boot + Vue.js Kubernetes Demo with JWT, Redis, MySQL, Prometheus, Grafana\",
    \"private\": false,
    \"auto_init\": false
  }")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "201" ]; then
    echo "✅ 仓库创建成功！"
    echo ""
    echo "仓库地址: https://github.com/$GITHUB_USER/$REPO_NAME"
    echo ""
    echo "现在可以推送代码了:"
    echo "  git push -u origin main"
elif [ "$HTTP_CODE" = "422" ]; then
    echo "⚠️  仓库可能已存在，尝试推送代码..."
    git push -u origin main
else
    echo "❌ 创建仓库失败 (HTTP $HTTP_CODE)"
    echo "响应: $BODY"
    exit 1
fi
