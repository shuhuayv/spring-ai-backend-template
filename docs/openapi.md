# Swagger / OpenAPI 接口文档

## 访问地址

项目启动后，通过以下地址访问 Swagger UI：

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON 描述文件：

```
http://localhost:8080/v3/api-docs
```

## 接口分组

Swagger UI 中接口按以下标签分组：

- **用户管理接口** — 用户 CRUD 和分页查询
- **AI 对话接口** — 模拟 AI 对话调用

## 注解说明

所有接口和模型均已添加中文注解，包括：

- `@Tag` — 接口分组标签
- `@Operation` — 接口操作说明
- `@Parameter` — 参数说明和示例值
- `@Schema` — 模型字段说明、示例值、必填标记

## 使用说明

1. 启动项目：`mvn spring-boot:run`
2. 打开浏览器访问 `http://localhost:8080/swagger-ui.html`
3. 选择目标接口分组，展开接口详情
4. 点击 "Try it out" 可在浏览器中直接测试接口

## 运行时已验证路径（Runtime Verified Paths）

在最终 main 的运行时验证中，`/v3/api-docs` 返回 200 且正确登记了以下两个业务路径：

- `GET /api/users/page` — 用户分页查询
- `POST /api/ai/chat` — 模拟 AI 对话

> 注意：OpenAPI 文档的存在**不等于**项目集成了 Spring AI 依赖。OpenAPI JSON 标题中的「Spring AI」仅来自项目名/标题文本，并非对 Spring AI framework 的依赖声明。