package com.shuhuayv.template.controller;

import com.shuhuayv.template.common.ApiResponse;
import com.shuhuayv.template.dto.UserCreateRequest;
import com.shuhuayv.template.dto.UserUpdateRequest;
import com.shuhuayv.template.entity.SysUser;
import com.shuhuayv.template.service.SysUserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping
    public ApiResponse<List<SysUser>> listUsers() {
        return ApiResponse.success(sysUserService.listUsers());
    }

    @GetMapping("/{id}")
    public ApiResponse<SysUser> getUserById(@PathVariable Long id) {
        return ApiResponse.success(sysUserService.getUserById(id));
    }

    @PostMapping
    public ApiResponse<SysUser> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.success(sysUserService.createUser(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<SysUser> updateUser(@PathVariable Long id,
                                           @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.success(sysUserService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return ApiResponse.success();
    }
}
