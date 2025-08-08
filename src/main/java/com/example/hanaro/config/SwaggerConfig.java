package com.example.hanaro.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Hanaro Shopping Mall API Docs",
                version = "v1",
                description = "디지털 하나로 금융서비스개발 6기 쇼핑몰 과제 API 명세서입니다. 🚀"
        ),
        servers = { @Server(url = "http://localhost:8080", description = "로컬 개발 서버") }
)
public class SwaggerConfig {

    // 사용자 API 그룹
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("USER API") // 정렬을 위해 접두 숫자 사용
                .packagesToScan("com.example.hanaro.domain.user.controller")
                .pathsToMatch("/api/user/**")
                .build();
    }

    // 관리자 API 그룹
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("ADMIN API")
                .packagesToScan("com.example.hanaro.domain.admin.controller")
                .pathsToMatch("/api/admin/**")
                .build();
    }
}
