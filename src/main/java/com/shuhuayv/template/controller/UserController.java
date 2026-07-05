package com.shuhuayv.template.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shuhuayv.template.common.ApiResponse;
import com.shuhuayv.template.common.PageResult;
import com.shuhuayv.template.dto.UserCreateRequest;
import com.shuhuayv.template.dto.UserUpdateRequest;
import com.shuhuayv.template.entity.SysUser;
import com.shuhuayv.template.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户管理接口", description = "提供用户查询、新增、修改、删除等基础接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Operation(summary = "查询用户列表", description = "获取所有用户信息列表")
    @GetMapping
    public ApiResponse<List<SysUser>> listUsers() {
        return ApiResponse.success(sysUserService.listUsers());
    }

    @Operation(summary = "分页查询用户", description = "分页获取用户信息列表")
    @GetMapping("/page")
    public ApiResponse<PageResult<SysUser>> pageUsers(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") long pageNum,
            @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") long pageSize) {
        IPage<SysUser> page = sysUserService.pageUsers(pageNum, pageSize);
        PageResult<SysUser> result = PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
        return ApiResponse.success(result);
    }

    @Operation(summary = "查询单个用户", description = "根据用户 ID 获取用户详细信息")
    @GetMapping("/{id}")
    public ApiResponse<SysUser> getUserById(
            @Parameter(description = "用户 ID", example = "1") @PathVariable Long id) {
        return ApiResponse.success(sysUserService.getUserById(id));
    }

    @Operation(summary = "新增用户", description = "创建一个新用户")
    @PostMapping
    public ApiResponse<SysUser> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.success(sysUserService.createUser(request));
    }

    @Operation(summary = "修改用户", description = "根据用户 ID 更新用户信息")
    @PutMapping("/{id}")
    public ApiResponse<SysUser> updateUser(
            @Parameter(description = "用户 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.success(sysUserService.updateUser(id, request));
    }

    @Operation(summary = "删除用户", description = "根据用户 ID 删除用户")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(
            @Parameter(description = "用户 ID", example = "1") @PathVariable Long id) {
        sysUserService.deleteUser(id);
        return ApiResponse.success();
    }
}
