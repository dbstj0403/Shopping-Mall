package com.example.hanaro.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 모든 @RestController의 예외를 전역에서 처리해주는 어노테이션
public class GlobalExceptionHandler {

    // JSR-303 유효성 검사 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class) // @Valid 실패로 발생하는 예외 (DTO의 notblank 등의 검증을 통과하지 못할 때 발생)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        for(FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "message", "유효성 검사에 실패했습니다.",
                "errors", errors
        ));
    }

    // 기타 예외 처리 (잘못된 입력 등 비즈니스 예외 처리)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "message", e.getMessage()
        ));
    }

    // 서버 내부 예외 처리 (500 에러)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> hanldeServerException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", 500,
                "message", "서버 내부 오류가 발생했습니다.",
                "detail", e.getMessage()
        ));
    }
}
