package com.shuhuayv.template.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Proves the pagination infrastructure is correctly wired:
 * {@link MybatisPlusConfig} registers a {@link MybatisPlusInterceptor}
 * containing a {@link PaginationInnerInterceptor} configured for MySQL.
 *
 * <p>No Spring context / no database is started — this only exercises the
 * {@code @Bean} factory method directly.</p>
 */
class MybatisPlusConfigTest {

    @Test
    void mybatisPlusInterceptorContainsMysqlPaginationInnerInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusConfig().mybatisPlusInterceptor();

        Optional<PaginationInnerInterceptor> pagination = interceptor.getInterceptors().stream()
                .filter(i -> i instanceof PaginationInnerInterceptor)
                .map(i -> (PaginationInnerInterceptor) i)
                .findFirst();

        assertTrue(pagination.isPresent(),
                "PaginationInnerInterceptor must be registered for physical pagination");
        assertEquals(DbType.MYSQL, pagination.get().getDbType(),
                "PaginationInnerInterceptor must target MySQL dialect");
    }
}
