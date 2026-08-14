package com.shuhuayv.template.ai.service.impl;

import com.shuhuayv.template.ai.dto.ChatRequest;
import com.shuhuayv.template.ai.dto.ChatResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiChatServiceImplTest {

    private final AiChatServiceImpl service = new AiChatServiceImpl();

    @Test
    void chat_nonBlankPrompt_returnsMockAnswerContainingPrompt() {
        ChatRequest request = new ChatRequest();
        request.setPrompt("介绍一下 Spring Boot");

        ChatResponse response = service.chat(request);

        assertNotNull(response.getRequestId());
        assertEquals("介绍一下 Spring Boot", response.getPrompt());
        assertTrue(response.getCostMs() >= 0);
        assertTrue(response.getAnswer().contains("介绍一下 Spring Boot"));
        assertTrue(response.getAnswer().startsWith("【Mock AI 响应】"));
    }

    @Test
    void chat_blankPrompt_returnsEmptyMessageAnswer() {
        ChatRequest request = new ChatRequest();
        request.setPrompt("   ");

        ChatResponse response = service.chat(request);

        assertEquals("收到了一条空消息。", response.getAnswer());
    }

    @Test
    void chat_nullPrompt_returnsEmptyMessageAnswer() {
        ChatRequest request = new ChatRequest();
        request.setPrompt(null);

        ChatResponse response = service.chat(request);

        assertEquals("收到了一条空消息。", response.getAnswer());
    }
}
