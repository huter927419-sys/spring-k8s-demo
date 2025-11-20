# GitHub 仓库设置指南

## 步骤 1: 在 GitHub 上创建新仓库

1. 登录 GitHub (https://github.com)
2. 点击右上角的 "+" 号，选择 "New repository"
3. 填写仓库信息：
   - Repository name: `spring-k8s-demo` (或您喜欢的名称)
   - Description: `Spring Boot + Vue.js Kubernetes Demo with JWT, Redis, MySQL, Prometheus, Grafana`
   - Visibility: Public 或 Private (根据您的需要)
   - **不要**勾选 "Initialize this repository with a README"
4. 点击 "Create repository"

## 步骤 2: 添加 SSH 公钥到 GitHub

1. 复制以下公钥内容：
```
ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIPAtTqnEUN92fwFCnyqpWmSV5Kxzl1C0Lh8VFpUtg3JM huter927419@gmail.com
```

2. 在 GitHub 上：
   - 点击右上角头像 → Settings
   - 左侧菜单选择 "SSH and GPG keys"
   - 点击 "New SSH key"
   - Title: `Server SSH Key`
   - Key: 粘贴上面的公钥
   - 点击 "Add SSH key"

## 步骤 3: 测试 SSH 连接

运行以下命令测试连接：
```bash
ssh -T git@github.com
```

如果看到 "Hi username! You've successfully authenticated..." 说明连接成功。

## 步骤 4: 添加远程仓库并推送

创建仓库后，运行以下命令（替换 YOUR_USERNAME 和 REPO_NAME）：

```bash
cd /data/k8s
git remote add origin git@github.com:YOUR_USERNAME/REPO_NAME.git
git branch -M main
git push -u origin main
```

例如，如果您的用户名是 `huter927419`，仓库名是 `spring-k8s-demo`：
```bash
git remote add origin git@github.com:huter927419/spring-k8s-demo.git
git branch -M main
git push -u origin main
```

## 当前状态

✅ Git 仓库已初始化
✅ 代码已提交到本地
✅ SSH 密钥已配置
⏳ 等待您创建 GitHub 仓库并添加远程地址

## 快速命令

如果您已经创建了仓库，直接运行：

```bash
cd /data/k8s
git remote add origin git@github.com:YOUR_USERNAME/REPO_NAME.git
git branch -M main
git push -u origin main
```
