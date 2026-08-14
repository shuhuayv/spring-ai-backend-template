# Interview Narrative — spring-ai-backend-template

> 三个时长的叙述版本。所有事实以 `FINAL_ENGINEERING_FACTS.md` 为准；严守边界：无 Spring AI 依赖、无 REAL LLM、无 Qdrant 集成。

## 30 秒版

这是一个 Java 21 / Spring Boot 4.1 的**可复用后端模板**。它把分层架构、统一返回、全局异常、MyBatis-Plus 分页、Redis 缓存、参数校验、OpenAPI 和 CI 做成一套标准脚手架。我重点做了分页正确性、HTTP 错误语义硬化、Mock AI 边界设计和 GitHub Actions CI，并用 15 个自动化测试守护核心契约。

## 90 秒版

我做了一个后端模板，核心是**工程实践的标准化**。架构上用 Controller / Service / Mapper 分层；统一 `ApiResponse` 返回，并用全局异常处理器把业务异常映射到正确的 HTTP 状态码（400/500），修复了早期「HTTP 200 + body 报错」的不规范语义。

数据层用 MyBatis-Plus，分页我特意处理了 3.5.9+ 之后 `PaginationInnerInterceptor` 需要独立 `mybatis-plus-jsqlparser` 依赖的坑，保证 `total`/`pages` 正确。缓存用 Redis 做用户详情 cache-aside（TTL 30 分钟，源码定义），更新/删除时 evict。

AI 模块目前是 **Mock 实现**——仓库名里有 Spring AI，但当前**没有 Spring AI 依赖、也没有接真实模型**，它只是演示 AI 服务契约和未来替换点。最后用 GitHub Actions CI 在 disposable MySQL/Redis 上跑完整测试和打包。

## 3 分钟版

我想讲清楚三件事：**为什么做、关键难点、当前边界**。

**为什么做**：很多后端项目都在重复搭基建。我把一套成熟的工程实践沉淀成一个模板——分层架构、统一返回、全局异常、分页、缓存、校验、OpenAPI、CI，让新项目能直接起步。

**关键难点一：分页元数据**。MyBatis-Plus 不注册 `PaginationInnerInterceptor` 时只会发 `LIMIT` 查询，`Page.getTotal()` 恒为 0，前端拿不到总条数。而且从 3.5.9 起这个拦截器依赖被拆到了独立的 `mybatis-plus-jsqlparser` 模块，不引入就分页失效。我按 `DbType.MYSQL` 配置好插件，并对 `pageNum/pageSize` 做边界校验（非法值直接 400），`PageResult` 出口再防御一层。

**关键难点二：HTTP 语义硬化**。早期出现过「HTTP 200 但 body 说 400/500」，网关和前端无法可靠判断。我让 `IllegalArgumentException` 和校验失败都映射到 400，未预期异常映射到 500，传输层和业务 code 对齐。

**关键难点三：隐私与边界**。AI 模块早期把完整 prompt 写进 INFO 日志，我移除了，只留 `requestId`/`costMs`。同时明确它**是 Mock**——不调用任何外部模型，仓库名里的 Spring AI 只是命名，不是依赖。

**CI 与验证**：GitHub Actions 用 disposable MySQL/Redis 跑完整 Maven 测试和打包，无密钥、不依赖本地环境。我有 15 个测试覆盖上下文加载、Mock AI、分页、防御式校验和插件装配。

**当前边界（诚实说明）**：它**不是生产系统**——没有认证/RBAC、没有分布式部署、没有负载基准；也**没有**接入真实大模型或 Qdrant。它是展示工程能力的作品集模板，未来可在 Service 实现层最小替换接入真实能力。
