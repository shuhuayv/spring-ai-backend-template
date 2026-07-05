package com.shuhuayv.template.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuhuayv.template.dto.UserCreateRequest;
import com.shuhuayv.template.dto.UserUpdateRequest;
import com.shuhuayv.template.entity.SysUser;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    List<SysUser> listUsers();

    SysUser getUserById(Long id);

    SysUser createUser(UserCreateRequest request);

    SysUser updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
}
