package com.example.hanaro.global.config;

import com.example.hanaro.global.swagger.examples.CommonExamples;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.responses.ApiResponse;

import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
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
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public GlobalOpenApiCustomizer globalResponseCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) return;

            boolean specHasGlobalSecurity =
                    openApi.getSecurity() != null && !openApi.getSecurity().isEmpty();

            openApi.getPaths().forEach((path, pathItem) ->
                    pathItem.readOperations().forEach(operation -> {
                        var responses = operation.getResponses();

                        responses.addApiResponse("500",
                                buildApiResponseWithExample("서버 오류", CommonExamples.SERVER_ERROR));

                        // 보안 필요한 경우만 401/403
                        boolean opHasSecurity = operation.getSecurity() != null && !operation.getSecurity().isEmpty();
                        boolean isAdminPath = path.startsWith("/api/admin/");
                        boolean isPublicPath = path.startsWith("/auth/") || path.startsWith("/public/");
                        boolean secured = (opHasSecurity || specHasGlobalSecurity || isAdminPath) && !isPublicPath;

                        if (secured) {
                            responses.addApiResponse("401",
                                    buildApiResponseWithExample("인증 필요 혹은 토큰 유효하지 않음", CommonExamples.UNAUTHORIZED));
                            responses.addApiResponse("403",
                                    buildApiResponseWithExample("권한 없음", CommonExamples.FORBIDDEN));
                        }
                    })
            );
        };
    }

    private io.swagger.v3.oas.models.responses.ApiResponse buildApiResponseWithExample(
            String description, String exampleJson
    ) {
        Schema<?> ref = new Schema<>().$ref("#/components/schemas/ApiResponseDto");

        Example ex = new Example().value(exampleJson);
        MediaType mt = new MediaType()
                .schema(ref)
                .addExamples("example", ex);

        Content content = new Content().addMediaType("application/json", mt);

        return new io.swagger.v3.oas.models.responses.ApiResponse()
                .description(description)
                .content(content);
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

    private ApiResponse buildApiResponse(String description) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType(
                        "application/json",
                        new MediaType().schema(
                                new Schema<>().$ref("#/components/schemas/ApiResponseDto")
                        )
                ));
    }
}
