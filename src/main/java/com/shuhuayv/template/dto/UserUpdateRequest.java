package com.shuhuayv.template.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateRequest {

    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;

    private Integer status;
}
