package com.example.hanaro.config.security;

import com.example.hanaro.global.error.ErrorCode;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Configuration
public class SecurityHandlerConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        // 인증 실패(토큰 없음/유효하지 않음) → 401
        return (req, res, ex) -> {
            writeErrorResponse(res, ErrorCode.UNAUTHORIZED);
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        // 인가는 됐지만 권한 없음 → 403
        return (req, res, ex) -> {
            writeErrorResponse(res, ErrorCode.FORBIDDEN);
        };
    }

    private void writeErrorResponse(HttpServletResponse res, ErrorCode errorCode) throws IOException {
        res.setStatus(errorCode.getStatus().value());
        res.setContentType("application/json;charset=UTF-8");

        ApiResponseDto<?> body = ApiResponseDto.fail(
                errorCode.getStatus().value(),
                errorCode.getMessage()
        );

        objectMapper.writeValue(res.getWriter(), body);
    }
}
