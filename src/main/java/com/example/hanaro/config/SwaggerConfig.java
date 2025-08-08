package com.example.hanaro.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.OpenApiCustomizer;
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
        servers = {
                @Server(url = "http://localhost:8080", description = "로컬 개발 서버")
        }
)
public class SwaggerConfig {

    // --- (1) 공통 응답 컴포넌트 & (옵션) JWT 보안 스키마 등록 ---
    @Bean
    public OpenAPI baseOpenAPI() {
        // ApiResponseDto는 제네릭이지만, 스키마 이름만 컴포넌트에 등록해 참조로 사용
        Schema<?> apiResponseSchemaRef = new Schema<>().$ref("#/components/schemas/ApiResponseDto");
        MediaType json = new MediaType().schema(apiResponseSchemaRef);
        Content jsonContent = new Content().addMediaType("application/json", json);

        ApiResponse badRequest = new ApiResponse().description("잘못된 요청").content(jsonContent);
        ApiResponse unauthorized = new ApiResponse().description("인증 필요").content(jsonContent);
        ApiResponse forbidden = new ApiResponse().description("접근 불가").content(jsonContent);
        ApiResponse notFound = new ApiResponse().description("리소스 없음").content(jsonContent);
        ApiResponse internal = new ApiResponse().description("서버 오류").content(jsonContent);

        Components components = new Components()
                // 스키마는 클래스 스캔으로 자동 생성되므로 이름 참조만 맞추면 됩니다.
                .addSchemas("ApiResponseDto", apiResponseSchemaRef)
                .addResponses("BadRequest", badRequest)
                .addResponses("Unauthorized", unauthorized)
                .addResponses("Forbidden", forbidden)
                .addResponses("NotFound", notFound)
                .addResponses("InternalServerError", internal);
                // ====== (옵션) JWT Bearer 보안 스키마 ======
//                .addSecuritySchemes("bearerAuth",
//                        new SecurityScheme()
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")
//                );

        // (옵션) 전역 보안 요구사항: 모든 API에 bearerAuth 적용
        SecurityRequirement security = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(components);
                // JWT를 안 쓴다면 아래 security는 제거해도 무방
//                .addSecurityItem(security);
    }

    // --- (2) 전 엔드포인트에 공통 응답 자동 주입 (springdoc 2.x: GlobalOpenApiCustomizer) ---
    @Bean
    public OpenApiCustomizer addGlobalResponses() {
        return openApi -> {
            if (openApi.getPaths() == null) return;
            openApi.getPaths().values().forEach(pathItem ->
                    pathItem.readOperations().forEach(op -> {
                        op.getResponses().addApiResponse("400",
                                new io.swagger.v3.oas.models.responses.ApiResponse().$ref("#/components/responses/BadRequest"));
                        op.getResponses().addApiResponse("401",
                                new io.swagger.v3.oas.models.responses.ApiResponse().$ref("#/components/responses/Unauthorized"));
                        op.getResponses().addApiResponse("403",
                                new io.swagger.v3.oas.models.responses.ApiResponse().$ref("#/components/responses/Forbidden"));
                        op.getResponses().addApiResponse("404",
                                new io.swagger.v3.oas.models.responses.ApiResponse().$ref("#/components/responses/NotFound"));
                        op.getResponses().addApiResponse("500",
                                new io.swagger.v3.oas.models.responses.ApiResponse().$ref("#/components/responses/InternalServerError"));
                    })
            );
        };
    }

    // --- (3) 사용자 API 그룹 ---
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("USER API")
                .packagesToScan("com.example.hanaro.domain.user.controller")
                .pathsToMatch("/api/user/**")
                .build();
    }

    // --- (4) 관리자 API 그룹 ---
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("ADMIN API")
                .packagesToScan("com.example.hanaro.domain.admin.controller")
                .pathsToMatch("/api/admin/**")
                .build();
    }
}
