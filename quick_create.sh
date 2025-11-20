#!/bin/bash

# 快速创建 GitHub 仓库脚本（使用 Token）

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "                    🚀 快速创建 GitHub 仓库"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# 检查是否设置了 Token
if [ -z "$GITHUB_TOKEN" ]; then
    echo "⚠️  需要设置 GITHUB_TOKEN 环境变量"
    echo ""
    echo "📋 获取 Token 步骤："
    echo "  1. 访问: https://github.com/settings/tokens"
    echo "  2. 点击 'Generate new token' -> 'Generate new token (classic)'"
    echo "  3. 设置 Note: 'Create Repo Token'"
    echo "  4. 勾选权限: 'repo' (完整仓库权限)"
    echo "  5. 点击 'Generate token'"
    echo "  6. 复制生成的 Token"
    echo ""
    echo "然后运行:"
    echo "  export GITHUB_TOKEN='your_token_here'"
    echo "  bash quick_create.sh"
    exit 1
fi

GITHUB_USER="huter927419-sys"
REPO_NAME="spring-k8s-demo"

echo "正在创建仓库: $GITHUB_USER/$REPO_NAME"
echo ""

# 使用 GitHub API 创建仓库
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
    echo "📦 仓库地址: https://github.com/$GITHUB_USER/$REPO_NAME"
    echo ""
    echo "正在推送代码..."
    
    # 确保远程仓库已配置
    git remote remove origin 2>/dev/null
    git remote add origin git@github.com:$GITHUB_USER/$REPO_NAME.git
    
    # 推送代码
    git push -u origin main
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "✅ 代码推送成功！"
        echo "🎉 完成！访问: https://github.com/$GITHUB_USER/$REPO_NAME"
    else
        echo ""
        echo "⚠️  代码推送失败，但仓库已创建"
        echo "您可以稍后手动推送: git push -u origin main"
    fi
elif [ "$HTTP_CODE" = "422" ]; then
    echo "⚠️  仓库可能已存在，尝试推送代码..."
    git remote remove origin 2>/dev/null
    git remote add origin git@github.com:$GITHUB_USER/$REPO_NAME.git
    git push -u origin main
else
    echo "❌ 创建仓库失败 (HTTP $HTTP_CODE)"
    echo "响应: $BODY"
    exit 1
fi
