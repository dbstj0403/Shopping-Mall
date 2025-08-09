package com.example.hanaro.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${file.base-upload}")
    private String baseUpload;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /static/** → 실제 프로젝트 폴더(src/main/resources/static)로 매핑
        String staticRoot = Paths.get("src/main/resources/static")
                .toAbsolutePath().toUri().toString(); // file:/.../src/main/resources/static/
        registry.addResourceHandler("/static/**")
                .addResourceLocations(staticRoot);
    }
}
