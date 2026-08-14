# Final Engineering Facts — spring-ai-backend-template

> 本文档是该 Backend Template 项目的**权威工程事实（authoritative engineering facts）**记录。
> 它在 Final Documentation Freeze Gate 收口，作为后续简历 / 面试 / 作品集陈述的单一事实源。

## Verified Engineering Baseline

```
9ea3cbdaa5dc7c56725bcb7aa1da816fe433ec80
```

- 这是**工程 / 源码基线（engineering/source baseline）**，对应 PR #1 的 merge commit。
- **它不是「未来会移动的仓库 HEAD」**。本 Gate 的文档提交会令 `main` 继续前进，因此本文档只把上面这个 commit 称为「Verified Engineering Baseline」，而把文档自身的提交/合并 commit 单独记录（见下文「Documentation Freeze」）。

## Current Verified Engineering

- **Java 21**（pom `java.version=21`）
- **Spring Boot 4.1.0**（spring-boot-starter-parent）
- **MyBatis-Plus 3.5.16**（`mybatis-plus-spring-boot4-starter`）
- **MyBatis-Plus JSqlParser 3.5.16**（`mybatis-plus-jsqlparser`，分页插件自 3.5.9+ 拆分出的独立模块）
- 物理分页：`MybatisPlusInterceptor` + `PaginationInnerInterceptor(DbType.MYSQL)`
- 统一返回：`ApiResponse<T>`（code / message / data）
- 全局异常硬化：`IllegalArgumentException` → 400/code400；`MethodArgumentNotValidException` → 400/code400；其他 → 500/code500
- Redis 用户详情缓存：cache-aside，`cache:user:{id}`，TTL **1800 秒（源码定义 `Duration.ofMinutes(30)`）**
- Mock AI 模块：`/api/ai/chat`，`AiChatServiceImpl` 写死模板回答，**无外部调用**
- OpenAPI：Springdoc OpenAPI 3，Swagger UI + `/v3/api-docs`
- 自动化测试：**15 用例 / 6 测试类 / 0 失败 / 0 错误 / 0 跳过**
- CI：GitHub Actions `Backend CI`，JDK 21 / Temurin，disposable MySQL + Redis service，完整 `./mvnw -B clean test` + `package -DskipTests`
- 最终 main 运行时验证（一次性本地基础设施）：应用启动成功；分页 / 边界 / Mock AI / OpenAPI 均通过；验证后一次性基础设施已销毁

## Capability Boundaries

明确**当前不包含**以下能力（不要误述）：

- ❌ **Spring AI framework 依赖**（pom 中无 `spring-ai` 库依赖；仓库名 `spring-ai-backend-template` 仅为命名）
- ❌ **REAL LLM provider**（OpenAI / Zhipu / Ollama 等均未接入）
- ❌ **Qdrant 应用层集成**（仅有 `docs/qdrant.md` 环境准备文档；无 SDK / client / vector service / collection 代码）
- ❌ 认证 / RBAC / 多租户
- ❌ 分布式部署 / 生产负载基准
- ❌ 生产 SaaS 能力

## Verification History / Accepted With Caveat

以下历史 / 过程类 caveat 必须保留，不得洗白为「全部 PASS」：

1. **早期运行时端口程序性违规**：某次 hardening Gate 曾在未授权情况下从被占用的 3306 切换至 alternate disposable MySQL 端口继续 runtime。分类：`PREVIOUS_RUNTIME_PORT_PROCEDURAL_RESULT=FAILED_CONSTRAINT`。后续 Gate 已明确授权 alternate disposable port 并重新验证，不影响最终技术结果。
2. **早期证据扫描器历史问题**：早期 scanner 曾错误排除 private/loopback numeric IPv4 并让绝对路径进入 evidence。分类：`PREVIOUS_EVIDENCE_SCANNER_HISTORICAL_RESULT=FAILED_THEN_CORRECTED`。后续 scanner contract 已修正，最终包实现为 0 home path / 0 numeric IPv4。
3. **交付 OpenAPI 派生 flag 假阴性**：上一交付 `FINAL_STATE_FLAGS.txt` 中 `PHASE22_OPENAPI_PASS=NO` / `RUNTIME_OVERALL=FAIL` 是**假阴性**——派生逻辑把项目名中的「Spring AI」误判为 Spring AI framework 依赖信号。技术结论：`PREVIOUS_DELIVERY_OPENAPI_TECHNICAL_RESULT=PASS`、`PREVIOUS_DELIVERY_RUNTIME_TECHNICAL_RESULT=PASS`。该 caveat 永久保留。
4. **pre-push 原始基线捕获部分缺失**：上一交付包中 `01_GIT_BASELINE.md` / `evidence/git/` 保存的是 merge 后 main=9ea3cb...，未保留一份独立明确的 pre-push 本地/远程原始基线。分类：`PREVIOUS_PRE_PUSH_RAW_BASELINE_CAPTURE=PARTIAL`。但技术交付仍被接受（基于 PR #1 metadata、PR head、ancestry、exact-head CI、current main live 一致等证据）。分类：`PREVIOUS_DELIVERY_PROCESS_EVIDENCE=PASS_WITH_PRE_PUSH_RAW_BASELINE_CAPTURE_CAVEAT`。

## Documentation Freeze

- Verified Engineering Baseline：`9ea3cbdaa5dc7c56725bcb7aa1da816fe433ec80`
- Documentation Commit：`DOC_COMMIT_SHA`（由本 Gate 的文档提交填入）
- Final Documentation Merge：`DOC_MERGE_COMMIT_SHA`（由本 Gate 的文档合并填入）

> 区分「工程基线」与「文档提交」可保证：即便文档合并后 `main` 继续前进，本文档描述的事实也不会因此变为 stale。
