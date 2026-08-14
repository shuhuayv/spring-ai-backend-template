package com.shuhuayv.template.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus infrastructure.
 *
 * <p>Enables physical pagination: {@code SysUserServiceImpl.pageUsers(...)} uses
 * {@code lambdaQuery().page(new Page<>(...))}. Without {@link PaginationInnerInterceptor}
 * MyBatis-Plus issues a plain {@code SELECT * ... LIMIT ?} and leaves
 * {@code Page.getTotal()} at 0, so pagination metadata is wrong at runtime.</p>
 *
 * <p>The application class already declares {@code @MapperScan}; this config only
 * registers the interceptor, it does not re-declare MapperScan.</p>
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
