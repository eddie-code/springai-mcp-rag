[toc]

# 目录

## 官方语下载

* [使用 Docker Compose 部署](https://docs.dify.ai/en/getting-started/install-self-hosted/docker-compose#customize-dify)
* [Dify+Nginx反向代理：80端口冲突的优雅解决方案](https://blog.csdn.net/icansoicrazy/article/details/149966625)
* [Ollama Win下载](https://ollama.com/download)

## 参考

* [Deepseek本地部署详细指南！从 Ollama 到个人知识库应用](https://blog.csdn.net/Code1994/article/details/145783923)
* Ollama 对电脑配置有一定要求

### 一、使用 Docker Compose 部署

#### 克隆 DIFY

将Dify源代码克隆到您的本地机器:

```text
git clone --branch "$(curl -s https://api.github.com/repos/langgenius/dify/releases/latest | jq -r .tag_name)" https://github.com/langgenius/dify.git
```

`如若无法下载，建议碰运气打开链接，直接工具下载：https://github.com/langgenius/dify/archive/refs/heads/main.zip`

#### 开始 DIFY

1.  导航到 Dify 源代码中的 Docker 目录
```shell
cd dify/docker
```
2.  复制环境配置文件 
```shell
cp .env.example .env
```
3.  启动 Docker 容器 
```shell
docker-compose up -d
```
4. 启动后会出现如下容器
```shell
[root@vbox docker]# docker ps
CONTAINER ID   IMAGE                                       COMMAND                  CREATED        STATUS                 PORTS                                                                                      NAMES
ab5881f3a056   nginx:latest                                "sh -c 'cp /docker-e…"   43 hours ago   Up 2 hours             0.0.0.0:7080->80/tcp, [::]:7080->80/tcp, 0.0.0.0:7443->443/tcp, [::]:7443->443/tcp         docker-nginx-1
c3a28de60efc   langgenius/dify-api:1.10.0                  "/bin/bash /entrypoi…"   43 hours ago   Up 2 hours             5001/tcp                                                                                   docker-worker-1
89bb3e691be1   langgenius/dify-api:1.10.0                  "/bin/bash /entrypoi…"   43 hours ago   Up 2 hours             5001/tcp                                                                                   docker-api-1
b3ca81d92a18   langgenius/dify-api:1.10.0                  "/bin/bash /entrypoi…"   43 hours ago   Up 2 hours             5001/tcp                                                                                   docker-worker_beat-1
97e7f122449b   langgenius/dify-plugin-daemon:0.4.1-local   "/bin/bash -c /app/e…"   43 hours ago   Up 2 hours             0.0.0.0:5003->5003/tcp, [::]:5003->5003/tcp                                                docker-plugin_daemon-1
66d35b9ed2ea   ubuntu/squid:latest                         "sh -c 'cp /docker-e…"   43 hours ago   Up 2 hours             3128/tcp                                                                                   docker-ssrf_proxy-1
d3bab8a2f16e   redis:6-alpine                              "docker-entrypoint.s…"   43 hours ago   Up 2 hours (healthy)   6379/tcp                                                                                   docker-redis-1
0caef156d9f3   semitechnologies/weaviate:1.27.0            "/bin/weaviate --hos…"   43 hours ago   Up 2 hours                                                                                                        docker-weaviate-1
b5cee0d665e9   postgres:15-alpine                          "docker-entrypoint.s…"   43 hours ago   Up 2 hours (healthy)   5432/tcp                                                                                   docker-db-1
6db7a8ef4db7   langgenius/dify-web:1.10.0                  "/bin/sh ./entrypoin…"   43 hours ago   Up 2 hours             3000/tcp                                                                                   docker-web-1
9e3d32916ea6   langgenius/dify-sandbox:0.2.12              "/main"                  43 hours ago   Up 2 hours (healthy)                                                                                              docker-sandbox-1
```

#### 修改 DIFY WEB端口

0. 进入变量文件进行修改
```shell
vim /dify/docker/.env
```
1. dify在docker容器内部的端口
```shell
NGINX_PORT=6060
NGINX_SSL_PORT=6443
```
2. 修改nginx对外暴漏的端口，是上面docker内部的端口对外的映射
```shell
EXPOSE_NGINX_PORT=6060   # 默认参数是80，这里修改为6060，
EXPOSE_NGINX_SSL_PORT=6443   # 默认参数是443，这里修改为6443
```
3. 修改API访问端口
```shell
SERVICE_API_URL=http://实际IP:6060
APP_API_URL=http://实际IP:6060
APP_WEB_URL=http://实际IP:6060
```

#### 升级 DIFY

输入 dify 源代码的 docker 目录并执行以下命令:

```shell
cd dify/docker
docker compose down
git pull origin main
docker compose pull
docker compose up -d
```

同步环境变量配置(重要)

* 如果.env.example文件已更新,请务必修改您的本地.env相应地归档。
* 检查并修改其中的配置项.env根据需要,请填写文件,以确保它们与您的实际环境相匹配。您可能需要从中添加任何新变量.env.example致您.env文件,并更新已更改的任何值。

#### 访问 Dify

访问管理员初始化页面以设置管理员账户:

```shell
# Local environment
http://localhost/install

# Server environment
http://your_server_ip/install
```

`开始打开网页会加载很慢`


### 二、Ollama 本地部署（可以跳过）

#### 下载与安装

* 下载对应系统安装：https://ollama.com/download
* 如若打开不了Github下载，Win版本直接使用工具下载：https://github.com/ollama/ollama/releases/latest/download/OllamaSetup.exe
* 访问 http://localhost:11434/

#### Deepseek 模型部署

**2.1 模型下载与加载**

以 deepseek r1 模型为例：

1. 访问https://ollama.com/library/deepseek-r1，默认为 7b 模型，如需其他模型，可以在当前页搜索所需模型
2. 模型详情页复制安装命令ollama run deepseek-r1
3. 安装完成后在终端执行：

```shell
Microsoft Windows [版本 10.0.26200.7171]
(c) Microsoft Corporation。保留所有权利。

C:\Users\23107>ollama -v
ollama version is 0.12.11

C:\Users\23107>ollama run deepseek-r1
pulling manifest
pulling e6a7edc1a4d7: 100% ▕██████████████████████████████████████████████████████████▏ 5.2 GB
pulling c5ad996bda6e: 100% ▕██████████████████████████████████████████████████████████▏  556 B
pulling 6e4c38e1172f: 100% ▕██████████████████████████████████████████████████████████▏ 1.1 KB
pulling ed8474dc73db: 100% ▕██████████████████████████████████████████████████████████▏  179 B
pulling f64cd5418e4b: 100% ▕██████████████████████████████████████████████████████████▏  487 B
verifying sha256 digest
writing manifest
success
>>> Send a message (/? for help)

C:\Users\23107>ollama rm deepseek-r1:latest
deleted 'deepseek-r1:latest'
```

* mac 后台标识
  * 见任务栏托盘区
* win 后台标识
  * 见任务栏托盘区

**2.2 模型验证测试**

运行交互式对话测试：

`请用Java写一个快速排序算法`

### 三、Ollama Docker部署

**创建一个确保 Ollama 服务正常运行的配置：**

* docker-compose-llm.yaml

```yaml
version: '3.8'

services:
  ollama:
    image: ollama/ollama:latest
    container_name: ollama
    restart: unless-stopped
    ports:
      - "11434:11434"
    volumes:
      - ../data/ollama_data:/root/.ollama
    environment:
      - OLLAMA_HOST=0.0.0.0
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:11434/api/tags"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s
```

然后执行：

```yaml
# 重新部署
docker-compose -f docker-compose-llm.yaml down
docker-compose -f docker-compose-llm.yaml up -d

# 等待健康检查通过
docker-compose -f docker-compose-llm.yaml ps

# 进入容器
docker exec -it ollama sh

# 拉取模型
ollama pull deepseek-r1:32b
```
`在WEB 配置 基础url 时，因为是 docker 服务，http://localhost:11434 存在无法访问的情况，可以尝试http://host.docker.internal:11434`
