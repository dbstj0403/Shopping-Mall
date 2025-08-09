package com.example.hanaro.global.error;

import com.example.hanaro.global.payload.response.ApiResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직에서 직접 던지는 CustomException 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponseDto.fail(errorCode));
    }

    /**
     * @Valid 유효성 검증 실패 (DTO 바인딩 시)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getDefaultMessage())
                .orElse(ErrorCode.INVALID_INPUT.getMessage());

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getStatus())
                .body(ApiResponseDto.fail(ErrorCode.INVALID_INPUT, errorMessage));
    }

    /**
     * @Validated + @RequestParam, @PathVariable 검증 실패
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .findFirst()
                .map(cv -> cv.getMessage())
                .orElse(ErrorCode.INVALID_INPUT.getMessage());

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getStatus())
                .body(ApiResponseDto.fail(ErrorCode.INVALID_INPUT, errorMessage));
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleException(Exception ex) {
        ex.printStackTrace(); // 로그
        return ResponseEntity
                .status(ErrorCode.INTERNAL_ERROR.getStatus())
                .body(ApiResponseDto.fail(ErrorCode.INTERNAL_ERROR));
    }
}
