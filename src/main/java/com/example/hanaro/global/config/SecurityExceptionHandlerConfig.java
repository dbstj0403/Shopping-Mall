package com.example.hanaro.global.config;

import com.example.hanaro.global.error.ErrorCode;
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
            om.writeValue(response.getWriter(), ApiResponseDto.fail(ErrorCode.UNAUTHORIZED)); // 401
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper om) {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            om.writeValue(response.getWriter(), ApiResponseDto.fail(ErrorCode.FORBIDDEN));    // 403
        };
    }
}
