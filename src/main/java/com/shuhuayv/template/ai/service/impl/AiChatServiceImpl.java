package com.shuhuayv.template.ai.service.impl;

import com.shuhuayv.template.ai.dto.ChatRequest;
import com.shuhuayv.template.ai.dto.ChatResponse;
import com.shuhuayv.template.ai.service.AiChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class AiChatServiceImpl implements AiChatService {

    @Override
    public ChatResponse chat(ChatRequest request) {
        long start = System.currentTimeMillis();

        String requestId = "req-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
                + "-" + UUID.randomUUID().toString().substring(0, 4);

        log.info("AI mock chat, requestId={}, prompt={}", requestId, request.getPrompt());

        String answer = buildMockAnswer(request.getPrompt());

        long costMs = System.currentTimeMillis() - start;
        log.info("AI mock chat done, requestId={}, costMs={}", requestId, costMs);

        return ChatResponse.builder()
                .requestId(requestId)
                .prompt(request.getPrompt())
                .answer(answer)
                .costMs(costMs)
                .build();
    }

    private String buildMockAnswer(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return "收到了一条空消息。";
        }
        return "【Mock AI 响应】这是一个模拟回答。你发送的提示词是：「" + prompt + "」。当前为 Mock 模式，后续可接入真实大模型 API 替换此实现。";
    }
}