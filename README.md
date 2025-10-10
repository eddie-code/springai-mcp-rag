# springai-mcp-rag

## （一）Redis Stack 

### 1.0 Redis命令行
docker exec -it redis bash

redis-cli -h 192.168.56.101 -p 6379 -a redis123 -n 10

### 1.1 切换到数据库 0
select 0

`Redis Stack 的搜索功能限制只能在数据库 0`

### 1.2 创建索引
FT.CREATE lee-vectorstore ON HASH PREFIX 1 "embedding:" SCHEMA content TEXT metadata TEXT embedding VECTOR FLAT 6 TYPE FLOAT32 DIM 1536 DISTANCE_METRIC COSINE

### 1.3 验证创建成功
FT._LIST

### 1.4 查看刚创建的索引信息
FT.INFO lee-vectorstore

### 1.5 测试插入数据
HSET embedding:doc1 content "测试文档" embedding "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

### 1.6 搜索测试
FT.SEARCH lee-vectorstore "*" LIMIT 0 10

### 1.7 删除索引
FT.DROPINDEX lee-vectorstore

### 1.8 删除索引但保留文档数据
FT.DROPINDEX lee-vectorstore DD

