# Qdrant 向量数据库

## 概述

Qdrant 是一个高性能的开源向量数据库，用于 AI 应用的向量存储和相似性搜索。

本项目（spring-ai-backend-template）后续将用于 `ai-knowledge-rag` 项目，Qdrant 将作为 RAG（检索增强生成）的向量存储引擎。

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

当前模板项目（`spring-ai-backend-template`）暂不直接依赖 Qdrant SDK，仅作为环境准备文档。向量存储的集成将在子项目（如 `ai-knowledge-rag`）中完成，本模板本身不包含相关依赖或代码。