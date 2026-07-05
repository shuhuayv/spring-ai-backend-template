# spring-ai-backend-template

Java + Spring Boot 后端公共模板项目，用于 7 月 AI 应用项目开发。

## 技术栈

- Java 21
- Spring Boot 4.1
- Maven
- MySQL
- Redis
- MyBatis-Plus
- Lombok
- Validation

## 已完成功能

- Spring Boot 基础项目
- MySQL 数据源配置
- Redis 基础配置
- MyBatis-Plus 集成
- 用户 CRUD
- Controller / Service / Mapper 三层架构
- 统一返回结构 ApiResponse
- 全局异常处理
- 参数校验

## 本地启动

### 1. 初始化数据库

mysql -uroot -p < sql/init.sql

### 2. 配置环境变量

参考 .env.example：

export DB_NAME=ai_template
export DB_USERNAME=ai_dev
export DB_PASSWORD=your_local_mysql_password
export REDIS_HOST=localhost
export REDIS_PORT=6379

也可以启动时临时指定：

DB_PASSWORD=your_local_mysql_password mvn spring-boot:run

### 3. 启动项目

mvn spring-boot:run

### 4. 测试接口

./scripts/test_user_crud.sh

## 用户接口

详见 docs/user-api.md。

## 当前接口

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | /api/users | 查询用户列表 |
| GET | /api/users/{id} | 按 ID 查询用户 |
| POST | /api/users | 新增用户 |
| PUT | /api/users/{id} | 修改用户 |
| DELETE | /api/users/{id} | 删除用户 |

## Redis 用户详情缓存

项目已对用户详情接口接入 Redis 缓存：

GET /api/users/{id}

缓存 Key 示例：

cache:user:1

缓存说明详见：

docs/redis-cache.md

缓存验证脚本：

./scripts/test_user_cache.sh
