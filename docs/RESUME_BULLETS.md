# Resume Bullets — spring-ai-backend-template

> 三版本简历要点。严格基于已核实事实：**不写** Spring AI 集成 / REAL LLM / Qdrant 集成 / 生产用户 / QPS / 准确率 / 微服务 / Kubernetes。

## A. Java Backend（Java 后端方向）

- Built a reusable Java 21 / Spring Boot 4.1 backend template with MyBatis-Plus, MySQL, Redis cache-aside patterns, validated pagination, Bean Validation, and OpenAPI documentation.
- Hardened REST contracts with HTTP-aligned error handling (400/500 mapped to consistent body codes) and verified MyBatis-Plus pagination metadata using `PaginationInnerInterceptor` + JSqlParser 3.5.16.
- Maintained 15 automated tests across 6 test classes with a GitHub Actions CI running the full suite against disposable MySQL/Redis.

## B. AI Application Infrastructure（AI 应用基础设施方向）

- Designed a Mock AI service boundary (`/api/ai/chat`) that demonstrates the AI-oriented controller/service contract and a clean future-replacement seam, with no external LLM dependency.
- Documented Qdrant local environment preparation as setup-only notes, keeping the application free of any vector-database integration.
- Removed full-prompt INFO logging in the AI module, retaining only `requestId`/`costMs` observables for privacy compliance.

## C. Engineering Reliability（工程可靠性方向）

- Added a GitHub Actions `Backend CI` (JDK 21 / Temurin) that runs the complete Maven test suite and packaging against disposable MySQL + Redis service containers — no secrets, no external REAL AI, no cross-project calls.
- Established a sealed-evidence closure workflow: privacy/secret scan, semantic fact audit, dynamic manifest + SHA256 seal, and re-extract verification for every delivery package.
- Froze authoritative engineering facts distinguishing the verified source baseline from moving documentation HEAD, with explicit historical caveats retained (no whitewashing of prior procedural/evidence issues).
