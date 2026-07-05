#!/usr/bin/env bash
set -e

BASE_URL="${BASE_URL:-http://localhost:8080}"
USER_ID="${USER_ID:-1}"
CACHE_KEY="cache:user:${USER_ID}"

echo "===== 1. 删除旧缓存 ====="
redis-cli del "$CACHE_KEY"

echo "===== 2. 第一次查询，应该 miss，查 MySQL，并写入 Redis ====="
curl -s "$BASE_URL/api/users/$USER_ID" | python3 -m json.tool

echo "===== 3. 查看 Redis Hash 缓存 ====="
redis-cli hgetall "$CACHE_KEY"

echo "===== 4. 查看缓存剩余时间 ====="
redis-cli ttl "$CACHE_KEY"

echo "===== 5. 第二次查询，应该 hit，直接读 Redis ====="
curl -s "$BASE_URL/api/users/$USER_ID" | python3 -m json.tool

echo "===== 6. 再次查看缓存剩余时间 ====="
redis-cli ttl "$CACHE_KEY"
