package com.shuhuayv.template.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springAiBackendTemplateOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring AI Backend Template API")
                        .version("v1.0.0")
                        .description("公共后端模板接口文档，包含用户 CRUD、MySQL、Redis 缓存等基础能力。"));
    }
}
