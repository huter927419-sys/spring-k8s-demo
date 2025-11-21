# 解决 Token 权限问题

## 问题
您的 Token 是 Fine-grained Token，缺少创建仓库的权限。

## 解决方案

### 方案 1: 创建 Classic Token（最简单，推荐）

1. 访问: https://github.com/settings/tokens/new
2. **重要**: 点击 "Generate new token" 下方的 **"Generate new token (classic)"**
3. 填写信息：
   - Note: `Create Repo Token`
   - Expiration: 根据需要选择
   - **勾选权限**: `repo` (完整仓库权限)
4. 点击 "Generate token"
5. 复制新 Token
6. 使用新 Token 创建仓库

### 方案 2: 更新 Fine-grained Token 权限

1. 访问: https://github.com/settings/tokens
2. 找到您的 Token，点击编辑
3. 在 "Repository permissions" 部分：
   - 找到 "Administration" 或 "Repository creation"
   - 设置为 "Read and write"
4. 保存更改
5. 重新尝试创建仓库

### 方案 3: 手动创建仓库（最快）

1. 访问: https://github.com/new
2. 填写信息并创建
3. 创建后告诉我，我来推送代码

## 使用新 Token 创建仓库

```bash
cd /data/k8s
export GITHUB_TOKEN='your_new_token_here'
bash quick_create.sh
```
