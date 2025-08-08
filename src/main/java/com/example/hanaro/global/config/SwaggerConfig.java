package com.example.hanaro.global.config;

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
                .addSchemas("ApiResponseDto", apiResponseSchemaRef)
                .addResponses("BadRequest", badRequest)
                .addResponses("Unauthorized", unauthorized)
                .addResponses("Forbidden", forbidden)
                .addResponses("NotFound", notFound)
                .addResponses("InternalServerError", internal)
                // JWT Bearer 스키마 등록
                .addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );

        return new OpenAPI()
                .components(components);
    }

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

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("USER API")
                .packagesToScan("com.example.hanaro.domain.user.controller")
                .pathsToMatch("/api/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("ADMIN API")
                .packagesToScan("com.example.hanaro.domain.admin.controller")
                .pathsToMatch("/api/admin/**")
                .addOpenApiCustomizer(openApi ->
                        openApi.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                )
                .build();
    }
}
