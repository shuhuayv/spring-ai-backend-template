package com.shuhuayv.template;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.shuhuayv.template.mapper")
@SpringBootApplication
public class SpringAiBackendTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiBackendTemplateApplication.class, args);
    }

}
