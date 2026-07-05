# AI 模块说明

## 概述

当前 AI 模块为 **Mock（模拟）实现**，不依赖任何真实大模型 API。

## 接口

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | /api/ai/chat | AI 对话（Mock） |

### 请求示例

```json
{
  "prompt": "你好，请介绍一下你自己"
}
```

### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "requestId": "req-20250705-163000-a1b2",
    "prompt": "你好，请介绍一下你自己",
    "answer": "【Mock AI 响应】这是一个模拟回答。你发送的提示词是：「你好，请介绍一下你自己」。当前为 Mock 模式，后续可接入真实大模型 API 替换此实现。",
    "costMs": 150
  }
}
```

## 模块结构

```
com.shuhuayv.template.ai
├── controller
│   └── AiChatController.java    # AI 对话接口
├── dto
│   ├── ChatRequest.java         # 对话请求 DTO
│   └── ChatResponse.java        # 对话响应 DTO
├── service
│   ├── AiChatService.java       # AI 服务接口
│   └── impl
│       └── AiChatServiceImpl.java  # Mock 实现
```

## 替换为真实大模型

后续可替换 `AiChatServiceImpl` 的实现，接入真实大模型 API，例如：

- Spring AI 框架（OpenAI / Ollama / 通义千问 等）
- 直接调用大模型 HTTP API

替换时只需修改 `AiChatServiceImpl` 的实现，无需改动 Controller 和 DTO。

## 安全提示

- 不要在代码中硬编码 API Key
- 建议通过环境变量或配置中心管理 API Key