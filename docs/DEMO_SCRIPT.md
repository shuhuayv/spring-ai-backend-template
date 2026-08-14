# Demo Script — spring-ai-backend-template

> 2–3 分钟演示脚本。**默认只读优先**；如演示 CRUD，须标注 optional 且仅用 disposable/local demo DB。演示中**不调用 REAL AI、不启动 Qdrant、不调用 RAG、不重置项目**。

## 演示流程

### 1. 项目定位（~20s）
- 说明：这是一个 Java 21 / Spring Boot 4.1 的**可复用后端模板**，展示分层架构、统一返回、分页、缓存、校验、OpenAPI 与 CI。
- 强调边界：当前**无 Spring AI 依赖、无 REAL LLM、无 Qdrant 集成**（仓库名中的 Spring AI 只是命名）。

### 2. 打开 Swagger（~20s）
- 启动后在浏览器打开 `http://localhost:8080/swagger-ui.html`。
- 指出两组标签：「用户管理接口」「AI 对话接口」，以及中文注解（`@Tag` / `@Operation` / `@Parameter` / `@Schema`）。

### 3. 用户列表 / 用户详情（~25s，只读）
- `GET /api/users/{id}`（如 `id=1`）→ 200，返回用户信息。
- 说明：`getUserById` 走 Redis cache-aside，Key 为 `cache:user:1`，TTL 30 分钟（源码定义）。

### 4. 分页（~35s）
- `GET /api/users/page?pageNum=1&pageSize=10` → 200，`code=0`，`data` 含 `total` / `pages` / `records`，元数据自洽。
- 解释：物理分页依赖 `PaginationInnerInterceptor` + JSqlParser，否则 `total` 恒为 0。

### 5. 非法分页参数 → HTTP 400（~25s）
- `GET /api/users/page?pageNum=0&pageSize=10` → **HTTP 400**，`code=400`。
- 另演示 `pageSize=0` / `pageSize=101` 同样 400。说明边界校验 + 全局异常硬化（HTTP 状态与 body code 对齐）。

### 6. Mock AI（~30s）
- `POST /api/ai/chat` body `{"prompt":"hello"}` → 200，`data.answer` 含「【Mock AI 响应】」。
- 强调：这是 **Mock 实现**，不调用任何外部模型；仓库名含 Spring AI ≠ 当前有 Spring AI 依赖。

### 7. 空 prompt → HTTP 400（~20s）
- `POST /api/ai/chat` body `{"prompt":""}` → **HTTP 400**（`@NotBlank` 校验失败）。说明 Validation 与 GlobalExceptionHandler 配合。

### 8. Redis 缓存设计说明（~20s）
- 简述 cache-aside：miss→查 DB→写 Redis；hit→直接返回；更新/删除 evict `cache:user:{id}`。
- 诚实边界：当前为**源码/静态核实** TTL，未做完整运行时 TTL 衰减基准。

### 9. 测试与 CI（~20s）
- 说明：15 用例 / 6 测试类 / 0 失败；GitHub Actions `Backend CI` 在 disposable MySQL/Redis 上跑完整测试与打包，无密钥、无外部 REAL AI。

### 10. 边界总结（~15s）
- 复述：无认证/RBAC、无分布式部署、无生产负载基准、无真实 AI / Qdrant 集成。它是工程实践作品集模板。

## 可选：演示 CRUD（optional，谨慎）
- 仅当使用 **disposable / 本地 demo 数据库** 时演示 `POST /api/users` / `PUT` / `DELETE`。
- 演示后建议清理对应数据，避免污染演示环境。
- **不要**把破坏性 CRUD 作为必需步骤；**不要**在默认 Demo 中重置项目或操作宿主数据库。

## 禁止项（Demo 红线）
- ❌ 调用 REAL AI / 外部大模型
- ❌ 启动 Qdrant 或声称已集成 Qdrant
- ❌ 调用 RAG 项目
- ❌ 重置 / 破坏宿主 MySQL(3306) 或宿主 Redis(6379)
