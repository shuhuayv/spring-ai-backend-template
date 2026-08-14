# Backend Template — 面试设计问答

> 面向实习面试的设计问答。所有事实以 `FINAL_ENGINEERING_FACTS.md` 为权威源。注意边界：当前**无 Spring AI 依赖、无 REAL LLM、无 Qdrant 集成**。

## 1. 为什么要做 Backend Template？

为了把「可复用的后端工程实践」沉淀成一个脚手架：统一的返回结构、全局异常处理、分层架构、分页、缓存、参数校验、OpenAPI 文档、CI。用它可以在新项目里快速起步，而不必每次重新搭一遍基建。

## 2. Controller / Service / Mapper 为什么分层？

- **Controller**：处理 HTTP 协议层（路由、参数接收、状态码），不含业务规则。
- **Service**：承载业务逻辑与事务边界。
- **Mapper**（MyBatis-Plus `IService` / `BaseMapper`）：只负责数据访问。
分层让关注点分离：协议、业务、存储各司其职，便于测试、复用与维护。

## 3. ApiResponse 有什么作用？

`ApiResponse<T>` 统一了 `{ code, message, data }` 结构。前端不需要为每个接口适配不同返回形状；成功/失败都走同一套契约，便于统一拦截与错误处理。

## 4. Validation 与 GlobalExceptionHandler 如何配合？

- `@NotBlank` / `@NotNull` 等注解在 Controller 参数上做**输入校验**。
- 校验失败抛出 `MethodArgumentNotValidException`。
- `GlobalExceptionHandler` 统一捕获它（以及 `IllegalArgumentException`、未预期异常），转换成标准 `ApiResponse` 并给出正确的 HTTP 状态码。
这样校验逻辑与异常处理解耦，且对外契约一致。

## 5. 为什么 HTTP transport status 必须和 body code 一致？

早期实现出现过「HTTP 200 + body code 400/500」——传输层说成功、业务层说失败，前端和网关无法可靠判断。硬化后：业务异常同时反映在 **HTTP 状态码** 与 **body code** 上（`IllegalArgumentException`/`MethodArgumentNotValidException` → 400；其他 → 500），语义一致、可被中间件正确路由。

## 6. 分页为什么不能只调用 Page 对象？

没有注册 `PaginationInnerInterceptor` 时，MyBatis-Plus 只会发 `SELECT ... LIMIT ?`，**不会改写出 count 查询**，`Page.getTotal()` 恒为 0、`pages` 也为 0——前端拿不到正确的总条数与总页数。必须用分页插件拦截并自动补 count，才能得到自洽的 `total`/`pages`/`records`。

## 7. MyBatis-Plus 3.5.9+ 分页为什么需要 JSqlParser 独立依赖？

从 3.5.9 起，`PaginationInnerInterceptor` 从主包拆分到独立模块 `mybatis-plus-jsqlparser`。它依赖 JSqlParser 来解析并改写 SQL（注入 count / 分页子查询）。不引入该依赖，分页插件无法完成 SQL 改写，物理分页失效。

## 8. MybatisPlusInterceptor / PaginationInnerInterceptor 做什么？

`MybatisPlusInterceptor` 是 MyBatis 的 `Interceptor` 容器；`PaginationInnerInterceptor(DbType.MYSQL)` 作为内部拦截器，识别分页 `Page` 参数、按数据库方言改写 SQL、自动执行并回填 count。本模板按 `DbType.MYSQL` 配置。

## 9. pageNum / pageSize 为什么要边界校验？

`pageNum` 应 ≥ 1（第 0 页无意义），`pageSize` 应落在合理区间（本模板 `1..100`）。越界值要么产生空结果、要么可能触发全表扫描/超大页，影响正确性与性能。非法值直接 **400** 拒绝，比静默返回空更安全。

## 10. 为什么 PageResult 也做 defensive validation？

`PageResult.of(...)` 对 `pageSize <= 0` 主动抛 `IllegalArgumentException`。这是**出口防御**：即使上游漏校验，响应构造阶段也会失败而不是返回错误元数据，避免把不一致的分页对象暴露给调用方。

## 11. Redis cache-aside 流程是什么？

读 `GET /api/users/{id}`：
1. 先查 Redis（`cache:user:{id}`）
2. miss → 查 MySQL → 写 Redis（带 TTL）→ 返回
3. hit → 直接返回缓存

写/删时 evict 对应 `cache:user:{id}`（更新后重建缓存），保证缓存与 DB 最终一致。

## 12. 为什么 TTL = 30min？

TTL 是缓存与数据库一致性 / 内存占用的折中。30 分钟足够覆盖会话级重复读取，又能在数据变更后（即便 evict 漏掉）在有限时间内自动失效，避免长期脏数据。当前由源码 `Duration.ofMinutes(30)` 定义。

## 13. 更新/删除为什么要 evict cache？

更新/删除会改 DB；若不清除旧缓存，后续读会命中 stale 值。模板在 `updateUser` / `deleteUser` 中显式 `stringRedisTemplate.delete(cache:user:{id})`，下次读取会回源重建，保证一致性。

## 14. 当前 Redis 验证到什么程度？

**源码 / 静态核实**：TTL 取自源码定义并阅读确认（`REDIS_TTL_VERIFICATION_LEVEL=STATIC_SOURCE_VERIFIED`）。最终交付门禁**没有**对「miss→写→hit→TTL 衰减」做完整运行时基准测量。可准确说「实现了 Redis 用户详情缓存，TTL 由源码定义为 30 分钟」。

## 15. Mock AI module 有什么工程价值？

它演示了「AI-oriented service 契约」如何落地：Controller 接收 `prompt`、Service 返回 `answer`（含 `requestId`/`costMs` 等可观测字段）。即便没有真实模型，也能把接口、DTO、错误契约、未来替换点设计完整，是干净的扩展骨架。

## 16. 为什么不把 Mock AI 说成 REAL AI？

诚实与可验证：当前 `AiChatServiceImpl` 返回写死模板，不调用任何外部模型。在简历/面试中把它说成「基于 Spring AI 的真实大模型服务」是**事实错误**，一旦被追问实现细节会暴露。正确说法是「Mock AI 边界 + 可替换扩展点」。

## 17. 为什么 repo 名字有 Spring AI 但当前没 Spring AI dependency？

`spring-ai-backend-template` 是**项目/历史命名**，不表示当前引入了 Spring AI framework。`pom.xml` 中没有任何 `spring-ai` 库依赖。仓库名 ≠ 依赖事实——判断依赖必须看 `pom.xml`，而非字符串匹配。

## 18. 如果未来接真实 LLM，最小替换点在哪里？

只需替换 `AiChatServiceImpl` 的实现：在其内部调用真实 provider（如 Spring AI / OpenAI HTTP API），Controller、DTO（`ChatRequest`/`ChatResponse`）、异常契约都不用动。这就是 Mock 边界的价值——替换点被收敛在 Service 实现内。

## 19. 为什么 prompt body 不应默认写 INFO log？

prompt 可能包含**用户隐私 / 敏感输入**。早期实现曾把完整 prompt 写入 INFO 日志，已移除——当前仅记录 `requestId`/`costMs` 等必要字段。最小化日志敏感面是隐私合规的基本纪律。

## 20. OpenAPI / Swagger 解决什么问题？

自动从代码注解生成机器可读的 API 描述（`/v3/api-docs`）与交互式 UI（`swagger-ui.html`），让前端/联调方无需读源码即可理解接口、参数、模型与示例，降低协作成本、减少契约漂移。

## 21. Qdrant docs 为什么不等于 Qdrant integration？

`docs/qdrant.md` 只是**环境准备文档**（如何起容器）。应用没有 Qdrant SDK、client、vector service 或 collection 代码，`QDRANT_APP_INTEGRATION=NO`。文档 ≠ 集成；向量能力属于独立项目/未来扩展。

## 22. CI 为什么需要 disposable MySQL / Redis？

测试需要真实数据存储与缓存才能跑通（如分页 count、上下文加载）。CI 用 **service 容器**提供一次性 MySQL/Redis，用完即弃、无密钥、不依赖本地环境，保证测试可复现且隔离。

## 23. 15 tests 主要保护哪些行为？

- `SpringAiBackendTemplateApplicationTests`：Spring 上下文能正常加载（集成冒烟）
- `AiChatServiceImplTest` / `AiChatControllerTest`：Mock AI 行为与边界（空 prompt → 400）
- `UserControllerPaginationTest`：分页元数据自洽、非法参数 → 400
- `PageResultTest`：`pageSize<=0` 防御式抛错
- `MybatisPlusConfigTest`：分页插件正确装配

覆盖了核心契约与回归点，但不是「高覆盖率测试体系」（无覆盖率承诺）。

## 24. 当前项目离 production 还缺什么？

认证/RBAC、限流、可观测性（metrics/tracing）、分布式/水平扩展、生产级配置与密钥管理、更完整的测试覆盖与负载基准、以及（若需要）真实的 AI / 向量检索集成。它是**工程实践模板**，不是生产 SaaS。
