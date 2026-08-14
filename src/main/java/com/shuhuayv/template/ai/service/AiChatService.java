package com.shuhuayv.template.ai.service;

import com.shuhuayv.template.ai.dto.ChatRequest;
import com.shuhuayv.template.ai.dto.ChatResponse;

public interface AiChatService {

    ChatResponse chat(ChatRequest request);
}