# Qdrant 向量数据库

## 概述

Qdrant 是一个高性能的开源向量数据库，用于 AI 应用的向量存储和相似性搜索。

本文件仅记录 Qdrant 的本地环境准备方式。当前 Backend Template **本身不集成 Qdrant**——应用没有 Qdrant SDK、client、vector service 或 collection 代码。向量数据库能力属于**独立项目或未来扩展范围**（例如独立的 RAG 项目），不在本模板的运行时依赖之内。

## Docker 启动命令

```bash
# 拉取镜像
docker pull qdrant/qdrant

# 启动容器（API 端口 6333，gRPC 端口 6334）
docker run -d --name qdrant \
  -p 6333:6333 \
  -p 6334:6334 \
  qdrant/qdrant
```

## 验证启动

```bash
curl http://localhost:6333
```

预期返回类似：

```json
{"title":"qdrant - vector search engine","version":"1.18.2"}
```

## 管理接口

| 地址 | 说明 |
|---|---|
| http://localhost:6333 | REST API |
| http://localhost:6334 | gRPC API |
| http://localhost:6333/dashboard | Web 管理面板 |

## 常用操作

```bash
# 查看运行状态
docker ps | grep qdrant

# 停止容器
docker stop qdrant

# 重新启动
docker start qdrant

# 删除容器（数据会丢失）
docker rm -f qdrant
```

## 当前集成状态

当前模板项目（`spring-ai-backend-template`）**不集成 Qdrant**：不直接依赖 Qdrant SDK，也不包含任何 client / vector service / collection 代码。Qdrant 向量能力属于**独立项目或未来扩展范围**，本模板仅提供上述环境准备文档。