package com.shuhuayv.template.ai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shuhuayv.template.ai.dto.ChatRequest;
import com.shuhuayv.template.ai.dto.ChatResponse;
import com.shuhuayv.template.ai.service.AiChatService;
import com.shuhuayv.template.exception.GlobalExceptionHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AiChatControllerTest {

    @Mock
    private AiChatService aiChatService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Validator validator;

    @BeforeEach
    void setUp() {
        AiChatController controller = new AiChatController(aiChatService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void chat_validPrompt_returns200AndCallsService() throws Exception {
        ChatRequest request = new ChatRequest();
        request.setPrompt("你好");

        ChatResponse mockResponse = ChatResponse.builder()
                .requestId("req-test-001")
                .prompt("你好")
                .answer("【Mock AI 响应】这是一个模拟回答。")
                .costMs(10L)
                .build();
        when(aiChatService.chat(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.requestId").value("req-test-001"));

        verify(aiChatService, times(1)).chat(any());
    }

    @Test
    void chat_blankPrompt_failsNotBlankValidation() {
        ChatRequest request = new ChatRequest();
        request.setPrompt("");

        Set<ConstraintViolation<ChatRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("提示词不能为空", violations.iterator().next().getMessage());
    }
}
