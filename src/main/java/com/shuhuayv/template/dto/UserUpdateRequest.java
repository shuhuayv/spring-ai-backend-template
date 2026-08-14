package com.shuhuayv.template.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Schema(description = "更新用户请求")
public class UserUpdateRequest {

    @Schema(description = "昵称", example = "新管理员")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "状态：1-启用，0-禁用", example = "1")
    private Integer status;
}
