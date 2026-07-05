# Redis Cache

本项目已接入 Redis，用于缓存用户详情接口。

## 缓存接口

GET /api/users/{id}

## 缓存 Key

cache:user:{id}

示例：

cache:user:1

## 缓存结构

使用 Redis Hash 存储用户字段：

- id
- username
- nickname
- email
- status
- createdAt
- updatedAt

## 缓存过期时间

30 分钟，即 1800 秒。

## 缓存流程

第一次查询用户详情：

1. 先查询 Redis
2. 如果 Redis 不存在，则查询 MySQL
3. 查询成功后写入 Redis
4. 返回用户信息

第二次查询用户详情：

1. 先查询 Redis
2. 如果 Redis 存在，则直接返回缓存数据
3. 不再查询 MySQL

## 缓存清理

修改用户时：

- 更新 MySQL
- 删除 cache:user:{id}
- 重新查询并写入新缓存

删除用户时：

- 删除 MySQL 数据
- 删除 cache:user:{id}

## 验证命令

删除缓存：

redis-cli del cache:user:1

第一次查询：

curl -s http://localhost:8080/api/users/1 | python3 -m json.tool

查看缓存：

redis-cli hgetall cache:user:1

查看 TTL：

redis-cli ttl cache:user:1

第二次查询：

curl -s http://localhost:8080/api/users/1 | python3 -m json.tool

后端日志应出现：

User cache miss, query database, key=cache:user:1
User cache hit, key=cache:user:1
