#!/usr/bin/env bash
set -e

BASE_URL="${BASE_URL:-http://localhost:8080}"

echo "===== 查询用户列表 ====="
curl -s "$BASE_URL/api/users" | python3 -m json.tool

echo "===== 按 ID 查询 admin ====="
curl -s "$BASE_URL/api/users/1" | python3 -m json.tool

echo "===== 新增用户 ====="
CREATE_RESP=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"test01","nickname":"测试用户","email":"test01@example.com","status":1}')

echo "$CREATE_RESP" | python3 -m json.tool

NEW_ID=$(echo "$CREATE_RESP" | python3 -c 'import sys,json; print(json.load(sys.stdin)["data"]["id"])')

echo "===== 修改用户 ====="
curl -s -X PUT "$BASE_URL/api/users/$NEW_ID" \
  -H "Content-Type: application/json" \
  -d '{"nickname":"测试用户已修改","email":"test01_new@example.com","status":1}' | python3 -m json.tool

echo "===== 删除用户 ====="
curl -s -X DELETE "$BASE_URL/api/users/$NEW_ID" | python3 -m json.tool

echo "===== 再次查询用户列表 ====="
curl -s "$BASE_URL/api/users" | python3 -m json.tool
