package com.shuhuayv.template.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;

    private Integer status = 1;
}
