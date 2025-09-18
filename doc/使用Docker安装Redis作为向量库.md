[toc]

# 目录

## redis与redis-stack的镜像区别

### Redis 镜像对比

| 特性 | `redis:7.0.7` | `redis-stack:latest`                             |
|:---|:---|:-------------------------------------------------|
| **内容组成** | 纯净的 Redis 服务器 | Redis 服务器 + 多个模块 + RedisInsight                  |
| **镜像大小** | 小 (~40MB) | 大 (~500MB)                                       |
| **主要用途** | 生产环境、轻量部署 | 开发、测试、原型设计                                       |
| **可视化工具** | 无，需要第三方工具 | 自带 RedisInsight (Web GUI)  http://localhost:8001 |
| **高级功能** | 无，仅核心数据结构 | 开箱即用 (RedisSearch, RedisJSON, RedisTimeSeries等)  |
| **部署复杂度** | 需要自行配置和集成 | 开箱即用，一体化部署                                       |
| **适用场景** | 高并发生产环境、微服务架构 | 本地开发、功能验证、快速原型                                   |
| **资源占用** | 低 | 高                                                |
| **扩展性** | 需要手动加载模块 | 预集成所有功能模块                                        |

### 使用示例

#### 启动 Redis 官方镜像

```bash
docker run --name my-redis -d -p 6379:6379 redis:7.0.7

## 拉取最新镜像
docker pull redis/redis-stack:latest

## 创建并运行一个容器，处于运行状态,7397 本地端口，6379容器内端口，其他的-p 8001:8001 是web端窗口可以要可以不要
docker run -d --name redis-stack -p 6379:6379 -e REDIS_ARGS="--requirepass 123456789" redis/redis-stack:latest
```

### docker-compose.yml

```yaml
services:
  redis:
    image: redis/redis-stack:latest
    container name: redis-stack
    ports:
      -"6379:6379"
    volumes:
      - ./redis/data:/data
      - restart:unless-stopped
```

```shell
docker-compose up -d
```