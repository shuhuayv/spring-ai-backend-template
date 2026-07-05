package com.shuhuayv.template.controller;

import com.shuhuayv.template.entity.SysUser;
import com.shuhuayv.template.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/api/users")
    public List<SysUser> listUsers() {
        return userMapper.selectList(null);
    }
}
