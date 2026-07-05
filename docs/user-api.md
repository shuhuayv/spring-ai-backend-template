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
| GET | /api/users/{id} | 按 ID 查询用户 |
| POST | /api/users | 新增用户 |
| PUT | /api/users/{id} | 修改用户 |
| DELETE | /api/users/{id} | 删除用户 |

## 新增用户示例

curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"test01","nickname":"测试用户","email":"test01@example.com","status":1}'

## 修改用户示例

curl -X PUT http://localhost:8080/api/users/2 \
  -H "Content-Type: application/json" \
  -d '{"nickname":"测试用户已修改","email":"test01_new@example.com","status":1}'
