package com.shuhuayv.template.ai.controller;

import com.shuhuayv.template.ai.dto.ChatRequest;
import com.shuhuayv.template.ai.dto.ChatResponse;
import com.shuhuayv.template.ai.service.AiChatService;
import com.shuhuayv.template.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AI 对话接口", description = "模拟 AI 对话调用，为后续 RAG 和代码评审项目预留")
@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @Operation(summary = "AI 对话", description = "发送提示词并获取 AI 模拟回答（当前为 Mock 实现）")
    @PostMapping("/chat")
    public ApiResponse<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        return ApiResponse.success(aiChatService.chat(request));
    }
}