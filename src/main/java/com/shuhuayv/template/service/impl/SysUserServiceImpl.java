package com.shuhuayv.template.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuhuayv.template.dto.UserCreateRequest;
import com.shuhuayv.template.dto.UserUpdateRequest;
import com.shuhuayv.template.entity.SysUser;
import com.shuhuayv.template.mapper.UserMapper;
import com.shuhuayv.template.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements SysUserService {

    @Override
    public List<SysUser> listUsers() {
        return list();
    }

    @Override
    public SysUser getUserById(Long id) {
        SysUser user = getById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
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
        return getById(id);
    }

    @Override
    public void deleteUser(Long id) {
        SysUser user = getById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        removeById(id);
    }
}
