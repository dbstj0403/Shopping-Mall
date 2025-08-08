package com.example.hanaro.global.config;

import com.example.hanaro.global.payload.response.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SecurityExceptionHandlerConfig {

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper om) {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            om.writeValue(response.getWriter(), ApiResponseDto.fail(401, "인증이 필요합니다."));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper om) {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            om.writeValue(response.getWriter(), ApiResponseDto.fail(403, "접근 권한이 없습니다."));
        };
    }
}
