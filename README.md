# spring-ai-backend-template

> Java 21 / Spring Boot 4.1 reusable backend template — demonstrates REST API, MySQL, Redis, MyBatis-Plus pagination, validation, OpenAPI, and a Mock AI module with a build/test CI.

## Positioning

这是一个**可复用的后端模板项目**（reusable backend template），用于快速搭建分层清晰的 CRUD 服务。它展示了标准化的工程实践，而非一个已经上线的生产系统。

> ⚠️ **仓库名与依赖是两个概念**：仓库名为 `spring-ai-backend-template`，但当前项目**不依赖 Spring AI framework**，也**没有接入任何真实大模型（REAL LLM）**。详见下方「Mock AI Boundary」与「Current Boundaries」。

## Tech Stack

- Java 21
- Spring Boot 4.1.0
- Maven（含 Maven Wrapper）
- MySQL 8（通过 MyBatis-Plus 访问）
- Redis 7（用户详情缓存）
- MyBatis-Plus 3.5.16
  - 物理分页依赖 `mybatis-plus-jsqlparser` 3.5.16（`PaginationInnerInterceptor` 自 MyBatis-Plus 3.5.9+ 拆分至该独立模块）
- Springdoc OpenAPI / Swagger UI（OpenAPI 3）
- Validation（Jakarta Bean Validation）
- Lombok

## Verified Capabilities

- 统一返回结构 `ApiResponse<T>`（`code` / `message` / `data`）
- 全局异常处理，HTTP 传输状态与业务 `code` 对齐
- 用户 CRUD（Controller / Service / Mapper 三层）
- 用户分页查询（MyBatis-Plus 物理分页，`PageResult` 防御式校验）
- 参数校验（`@NotBlank` / `@NotNull` 等）
- Redis 缓存用户详情（cache-aside，TTL 30 分钟，源码定义）
- 模拟 AI 对话模块（`/api/ai/chat`，Mock 实现）
- OpenAPI / Swagger 中文注解
- GitHub Actions CI：完整 `./mvnw -B clean test` + `package`
- 自动化测试：15 用例 / 6 测试类 / 0 失败 / 0 错误 / 0 跳过

## API Overview

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/users` | 查询用户列表 |
| GET | `/api/users/page?pageNum=1&pageSize=10` | 分页查询用户 |
| GET | `/api/users/{id}` | 按 ID 查询用户（走 Redis 缓存） |
| POST | `/api/users` | 新增用户 |
| PUT | `/api/users/{id}` | 修改用户（同步 evict 缓存） |
| DELETE | `/api/users/{id}` | 删除用户（同步 evict 缓存） |
| POST | `/api/ai/chat` | AI 对话（Mock） |

Swagger UI（默认端口）：`http://localhost:8080/swagger-ui.html`
OpenAPI JSON：`http://localhost:8080/v3/api-docs`

## Mock AI Boundary

> ⚠️ **当前 AI 模块为 Mock 实现**：`AiChatServiceImpl` 返回的是模板化/写死的模拟回答，**不调用任何外部大模型 API**。项目当前**没有 Spring AI 依赖**、**没有 REAL LLM provider**。该模块的价值在于演示「AI-oriented service / controller 契约」与边界设计，未来可替换为真实 providers（如 Spring AI / OpenAI / Ollama），但「替换为真实模型」是**扩展点**，不是当前能力。

## Pagination / Error Handling

- 分页：`GET /api/users/page?pageNum=1&pageSize=10`
  - 边界：`pageNum >= 1`，`1 <= pageSize <= 100`
  - 非法参数（`pageNum=0` / `pageSize=0` / `pageSize=101`）→ **HTTP 400** + 业务 `code=400`
- `PageResult.of(...)` 对 `pageSize <= 0` 显式抛出 `IllegalArgumentException`
- 全局异常映射：
  - `IllegalArgumentException` → HTTP 400 / code 400
  - `MethodArgumentNotValidException`（参数校验失败）→ HTTP 400 / code 400
  - 其他未预期异常 → HTTP 500 / code 500

> 这一硬化修复了早期「HTTP 200 + body 400/500」的不规范传输语义：业务异常现在会同时反映在 HTTP 状态码与 body `code` 上。

## Redis Cache

- 缓存接口：`GET /api/users/{id}`
- 缓存 Key：`cache:user:{id}`（如 `cache:user:1`）
- 存储结构：Redis Hash（user 字段）
- TTL：**30 分钟 = 1800 秒**（源码 `Duration.ofMinutes(30)`，静态/源码级核实）
- 流程：cache-aside（miss → 查 MySQL → 写 Redis；hit → 直接返回）；更新/删除时 evict 对应 `cache:user:{id}`
- 验证级别：当前为**源码/静态核实**（`REDIS_TTL_VERIFICATION_LEVEL=STATIC_SOURCE_VERIFIED`），未对最终交付门禁做完整「miss→写→hit→TTL 衰减」运行时基准测量

## OpenAPI

- 使用 Springdoc OpenAPI 为接口与模型添加中文注解（`@Tag` / `@Operation` / `@Parameter` / `@Schema`）
- 运行时已验证 `/v3/api-docs` 可访问，并正确登记 `/api/users/page` 与 `/api/ai/chat` 两个路径
- OpenAPI 文档本身**不等同于**「项目集成了 Spring AI 依赖」——「Spring AI」仅出现在项目名/标题文本中

## Tests & CI

- 自动化测试：**15 用例 / 6 测试类 / 0 失败 / 0 错误 / 0 跳过**
  - `SpringAiBackendTemplateApplicationTests`（上下文加载）
  - `AiChatServiceImplTest`、`AiChatControllerTest`
  - `UserControllerPaginationTest`、`PageResultTest`
  - `MybatisPlusConfigTest`
- GitHub Actions —— `Backend CI`：
  - 运行环境 JDK 21 / Temurin
  - 使用 **disposable MySQL service + disposable Redis service**（CI 内临时容器，无密钥、无外部 REAL AI、无 Qdrant/RAG/Reviewer 调用）
  - 执行完整 `./mvnw -B clean test` 与 `./mvnw -B package -DskipTests`
- 这构成 **build/test CI**，**不是生产部署流水线**（请勿表述为 production deployment pipeline 或声称「高覆盖率测试体系」）

## Local Run

### 1. 初始化数据库

```bash
mysql -uroot -p < sql/init.sql
```

### 2. 配置环境变量

参考 `.env.example`：

```bash
export DB_NAME=ai_template
export DB_USERNAME=ai_dev
export DB_PASSWORD=your_local_mysql_password
export REDIS_HOST=localhost
export REDIS_PORT=6379
```

也可在启动时临时指定：

```bash
DB_PASSWORD=your_local_mysql_password mvn spring-boot:run
```

### 3. 启动项目

```bash
mvn spring-boot:run
```

### 4. 测试接口

```bash
./scripts/test_user_crud.sh
```

## Current Boundaries

项目当前**明确不包含**以下能力（请勿在简历/面试中误述）：

- ❌ Spring AI framework 依赖或集成
- ❌ 任何 REAL LLM provider 调用（OpenAI / Zhipu / Ollama 等）
- ❌ Qdrant 应用层集成（仅有 `docs/qdrant.md` 环境准备文档）
- ❌ 认证 / RBAC / 多租户
- ❌ 分布式部署 / 生产负载基准
- ❌ 生产 SaaS 能力

它是一个**工程实践模板**，适合作为实习作品集展示分层架构、分页正确性、缓存设计、契约硬化与 CI 能力。

## 参考文档

- [docs/ai-module.md](docs/ai-module.md) — AI 模块与当前已核实边界
- [docs/openapi.md](docs/openapi.md) — Swagger / OpenAPI 用法与运行时已验证路径
- [docs/redis-cache.md](docs/redis-cache.md) — Redis 缓存设计与 TTL
- [docs/qdrant.md](docs/qdrant.md) — Qdrant 仅环境准备文档（非集成）
- [docs/user-api.md](docs/user-api.md) — 用户接口与错误契约
- [docs/FINAL_ENGINEERING_FACTS.md](docs/FINAL_ENGINEERING_FACTS.md) — 权威工程事实
- [docs/INTERVIEW_BACKEND_TEMPLATE_DESIGN.md](docs/INTERVIEW_BACKEND_TEMPLATE_DESIGN.md) — 面试设计问答
- [docs/RESUME_BULLETS.md](docs/RESUME_BULLETS.md) — 简历要点（三版本）
- [docs/INTERVIEW_NARRATIVE.md](docs/INTERVIEW_NARRATIVE.md) — 面试叙述（30s / 90s / 3min）
- [docs/DEMO_SCRIPT.md](docs/DEMO_SCRIPT.md) — 演示脚本
