# 使用命令创建 GitHub 仓库

## 方法 1: 使用 GitHub CLI (推荐)

### 安装 GitHub CLI

```bash
# Ubuntu/Debian
curl -fsSL https://cli.github.com/packages/githubcli-archive-keyring.gpg | sudo dd of=/usr/share/keyrings/githubcli-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" | sudo tee /etc/apt/sources.list.d/github-cli.list > /dev/null
sudo apt update
sudo apt install gh

# 或者使用 snap
sudo snap install gh
```

### 登录 GitHub

```bash
gh auth login
# 选择 GitHub.com
# 选择 HTTPS
# 选择 Login with a web browser
# 复制显示的代码，在浏览器中完成认证
```

### 创建仓库

```bash
cd /data/k8s
gh repo create huter927419-sys/spring-k8s-demo \
  --public \
  --description "Spring Boot + Vue.js Kubernetes Demo with JWT, Redis, MySQL, Prometheus, Grafana" \
  --source=. \
  --remote=origin \
  --push
```

## 方法 2: 使用 GitHub API + curl

### 1. 获取 Personal Access Token

1. 访问: https://github.com/settings/tokens
2. 点击 "Generate new token" -> "Generate new token (classic)"
3. 设置:
   - Note: `Create Repo Token`
   - Expiration: 根据需要选择
   - Scopes: 勾选 `repo` (完整仓库权限)
4. 点击 "Generate token"
5. 复制生成的 Token（只显示一次）

### 2. 创建仓库

```bash
cd /data/k8s

# 设置 Token
export GITHUB_TOKEN='your_token_here'

# 使用提供的脚本
bash create_repo.sh

# 或者直接使用 curl
curl -X POST \
  -H "Authorization: token $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github.v3+json" \
  https://api.github.com/user/repos \
  -d '{
    "name": "spring-k8s-demo",
    "description": "Spring Boot + Vue.js Kubernetes Demo with JWT, Redis, MySQL, Prometheus, Grafana",
    "private": false,
    "auto_init": false
  }'

# 然后推送代码
git push -u origin main
```

## 方法 3: 使用 GitHub Web UI (最简单)

1. 访问: https://github.com/new
2. 填写信息并创建
3. 然后推送代码

## 推荐流程

如果已安装 GitHub CLI，使用方法 1 最简单。
如果没有，使用方法 2 的脚本（create_repo.sh）也很方便。
