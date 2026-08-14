package com.shuhuayv.template.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "AI 对话请求")
public class ChatRequest {

    @NotBlank(message = "提示词不能为空")
    @Schema(description = "用户提示词", requiredMode = Schema.RequiredMode.REQUIRED, example = "你好，请介绍一下你自己")
    private String prompt;
}