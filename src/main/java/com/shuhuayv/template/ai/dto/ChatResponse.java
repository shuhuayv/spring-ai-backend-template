package com.shuhuayv.template.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI 对话响应")
public class ChatResponse {

    @Schema(description = "请求唯一标识", example = "req-20250705-001")
    private String requestId;

    @Schema(description = "用户原始提示词", example = "你好，请介绍一下你自己")
    private String prompt;

    @Schema(description = "AI 模拟回答", example = "你好！我是一个基于 Spring AI 的智能助手。")
    private String answer;

    @Schema(description = "处理耗时（毫秒）", example = "150")
    private long costMs;
}