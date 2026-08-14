package com.shuhuayv.template.common;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageResultTest {

    @Test
    void of_computesPagesCorrectly() {
        PageResult<String> r = PageResult.of(1, 10, 100, Collections.emptyList());
        assertEquals(1, r.getPageNum());
        assertEquals(10, r.getPageSize());
        assertEquals(100, r.getTotal());
        assertEquals(10, r.getPages());
    }

    @Test
    void of_zeroTotal_pagesIsZero() {
        PageResult<String> r = PageResult.of(1, 10, 0, Collections.emptyList());
        assertEquals(0, r.getPages());
    }

    @Test
    void of_nonDivisibleTotal_ceilPages() {
        PageResult<String> r = PageResult.of(1, 10, 95, Collections.emptyList());
        assertEquals(10, r.getPages());
    }

    @Test
    void of_zeroPageSize_doesNotDivideByZero_andPreservesOriginalPageSize() {
        PageResult<String> r = PageResult.of(1, 0, 100, Collections.emptyList());
        assertEquals(100, r.getPages());
        assertEquals(0, r.getPageSize());
    }
}
