#!/bin/bash

# GitHub CLI 登录和创建仓库脚本

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "                    🔐 GitHub CLI 登录和创建仓库"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# 检查是否已登录
if gh auth status &>/dev/null; then
    echo "✅ 已登录 GitHub"
else
    echo "📋 需要登录 GitHub"
    echo ""
    echo "方法 1: 使用浏览器登录（推荐）"
    echo "  运行: gh auth login"
    echo "  然后按照提示在浏览器中完成认证"
    echo ""
    echo "方法 2: 使用 Token 登录"
    echo "  1. 访问: https://github.com/settings/tokens"
    echo "  2. 生成新 Token (classic)，勾选 'repo' 权限"
    echo "  3. 运行: gh auth login --with-token < token.txt"
    echo "     (将 Token 保存到 token.txt)"
    echo ""
    read -p "是否现在登录？(y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        gh auth login
    else
        echo "请稍后手动登录，然后运行此脚本创建仓库"
        exit 0
    fi
fi

# 创建仓库
echo ""
echo "正在创建仓库..."
gh repo create huter927419-sys/spring-k8s-demo \
  --public \
  --description "Spring Boot + Vue.js Kubernetes Demo with JWT, Redis, MySQL, Prometheus, Grafana" \
  --source=. \
  --remote=origin \
  --push

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 仓库创建并推送成功！"
    echo "📦 仓库地址: https://github.com/huter927419-sys/spring-k8s-demo"
else
    echo ""
    echo "❌ 创建仓库失败，请检查错误信息"
fi
