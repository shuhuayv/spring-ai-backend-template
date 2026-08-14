package com.shuhuayv.template.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shuhuayv.template.entity.SysUser;
import com.shuhuayv.template.exception.GlobalExceptionHandler;
import com.shuhuayv.template.service.SysUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerPaginationTest {

    @Mock
    private SysUserService sysUserService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        UserController controller = new UserController(sysUserService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private IPage<SysUser> buildMockPage(long current, long size, long total, List<SysUser> records) {
        IPage<SysUser> page = mock(IPage.class);
        when(page.getCurrent()).thenReturn(current);
        when(page.getSize()).thenReturn(size);
        when(page.getTotal()).thenReturn(total);
        when(page.getRecords()).thenReturn(records);
        return page;
    }

    @Test
    void pageUsers_validParams_returns200WithPaginationMetadata() throws Exception {
        IPage<SysUser> mockPage = buildMockPage(1L, 10L, 100L, Collections.emptyList());
        when(sysUserService.pageUsers(1L, 10L)).thenReturn(mockPage);

        mockMvc.perform(get("/api/users/page").param("pageNum", "1").param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.pageNum").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.total").value(100))
                .andExpect(jsonPath("$.data.pages").value(10));

        verify(sysUserService, times(1)).pageUsers(1L, 10L);
    }

    @Test
    void pageUsers_pageNumZero_returns400AndDoesNotCallService() throws Exception {
        mockMvc.perform(get("/api/users/page").param("pageNum", "0").param("pageSize", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
        verify(sysUserService, never()).pageUsers(anyLong(), anyLong());
    }

    @Test
    void pageUsers_pageSizeZero_returns400AndDoesNotCallService() throws Exception {
        mockMvc.perform(get("/api/users/page").param("pageNum", "1").param("pageSize", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
        verify(sysUserService, never()).pageUsers(anyLong(), anyLong());
    }

    @Test
    void pageUsers_pageSizeOver100_returns400AndDoesNotCallService() throws Exception {
        mockMvc.perform(get("/api/users/page").param("pageNum", "1").param("pageSize", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
        verify(sysUserService, never()).pageUsers(anyLong(), anyLong());
    }
}
