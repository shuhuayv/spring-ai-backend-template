# User API

## 统一返回格式

JSON 返回结构：

{
  "code": 0,
  "message": "success",
  "data": {}
}

## 接口列表

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | /api/users | 查询用户列表 |
| GET | /api/users/page?pageNum=1&pageSize=10 | 分页查询用户 |
| GET | /api/users/{id} | 按 ID 查询用户 |
| POST | /api/users | 新增用户 |
| PUT | /api/users/{id} | 修改用户 |
| DELETE | /api/users/{id} | 删除用户 |

## 分页查询

`GET /api/users/page?pageNum=1&pageSize=10`

- 响应结构（`PageResult`）：`{ code, message, data: { pageNum, pageSize, total, pages, records } }`
- 边界约束：`pageNum >= 1`，`1 <= pageSize <= 100`
- 非法参数示例：`pageNum=0` / `pageSize=0` / `pageSize=101` → **HTTP 400**，body `code=400`
- `PageResult.of(...)` 对 `pageSize <= 0` 显式抛出 `IllegalArgumentException`

## 错误契约（Error Contract）

统一返回结构 `ApiResponse`：`{ code, message, data }`。

| 异常来源 | HTTP 状态 | body code |
|---|---|---|
| `IllegalArgumentException`（业务参数错误，如用户不存在 / 用户名为空） | 400 | 400 |
| `MethodArgumentNotValidException`（Bean Validation 失败，如 prompt 为空） | 400 | 400 |
| 其他未预期异常 | 500 | 500 |

> HTTP 传输状态与业务 `code` 保持一致：不再出现早期「HTTP 200 + body 400/500」的不规范语义。

## 新增用户示例

curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"test01","nickname":"测试用户","email":"test01@example.com","status":1}'

## 修改用户示例

curl -X PUT http://localhost:8080/api/users/2 \
  -H "Content-Type: application/json" \
  -d '{"nickname":"测试用户已修改","email":"test01_new@example.com","status":1}'
