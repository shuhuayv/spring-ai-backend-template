package com.shuhuayv.template.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuhuayv.template.dto.UserCreateRequest;
import com.shuhuayv.template.dto.UserUpdateRequest;
import com.shuhuayv.template.entity.SysUser;
import com.shuhuayv.template.mapper.UserMapper;
import com.shuhuayv.template.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements SysUserService {

    private static final String USER_CACHE_KEY_PREFIX = "cache:user:";
    private static final Duration USER_CACHE_TTL = Duration.ofMinutes(30);

    private final StringRedisTemplate stringRedisTemplate;

    public SysUserServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public List<SysUser> listUsers() {
        return list();
    }

    @Override
    public IPage<SysUser> pageUsers(long pageNum, long pageSize) {
        return lambdaQuery()
                .orderByDesc(SysUser::getId)
                .page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize));
    }

    @Override
    public SysUser getUserById(Long id) {
        String cacheKey = buildUserCacheKey(id);

        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
        Map<String, String> cachedUserMap = hashOps.entries(cacheKey);

        if (!CollectionUtils.isEmpty(cachedUserMap)) {
            log.info("User cache hit, key={}", cacheKey);
            return mapToUser(cachedUserMap);
        }

        log.info("User cache miss, query database, key={}", cacheKey);
        SysUser user = getById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        hashOps.putAll(cacheKey, userToMap(user));
        stringRedisTemplate.expire(cacheKey, USER_CACHE_TTL);

        return user;
    }

    @Override
    public SysUser createUser(UserCreateRequest request) {
        Long count = lambdaQuery()
                .eq(SysUser::getUsername, request.getUsername())
                .count();

        if (count > 0) {
            throw new IllegalArgumentException("用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setStatus(request.getStatus() == null ? 1 : request.getStatus());

        save(user);
        return getById(user.getId());
    }

    @Override
    public SysUser updateUser(Long id, UserUpdateRequest request) {
        SysUser user = getById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        updateById(user);
        stringRedisTemplate.delete(buildUserCacheKey(id));

        return getUserById(id);
    }

    @Override
    public void deleteUser(Long id) {
        SysUser user = getById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        removeById(id);
        stringRedisTemplate.delete(buildUserCacheKey(id));
    }

    private String buildUserCacheKey(Long id) {
        return USER_CACHE_KEY_PREFIX + id;
    }

    private Map<String, String> userToMap(SysUser user) {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(user.getId()));
        map.put("username", user.getUsername());

        if (user.getNickname() != null) {
            map.put("nickname", user.getNickname());
        }
        if (user.getEmail() != null) {
            map.put("email", user.getEmail());
        }
        if (user.getStatus() != null) {
            map.put("status", String.valueOf(user.getStatus()));
        }
        if (user.getCreatedAt() != null) {
            map.put("createdAt", user.getCreatedAt().toString());
        }
        if (user.getUpdatedAt() != null) {
            map.put("updatedAt", user.getUpdatedAt().toString());
        }

        return map;
    }

    private SysUser mapToUser(Map<String, String> map) {
        SysUser user = new SysUser();

        if (StringUtils.hasText(map.get("id"))) {
            user.setId(Long.valueOf(map.get("id")));
        }
        user.setUsername(map.get("username"));
        user.setNickname(map.get("nickname"));
        user.setEmail(map.get("email"));

        if (StringUtils.hasText(map.get("status"))) {
            user.setStatus(Integer.valueOf(map.get("status")));
        }
        if (StringUtils.hasText(map.get("createdAt"))) {
            user.setCreatedAt(LocalDateTime.parse(map.get("createdAt")));
        }
        if (StringUtils.hasText(map.get("updatedAt"))) {
            user.setUpdatedAt(LocalDateTime.parse(map.get("updatedAt")));
        }

        return user;
    }
}
