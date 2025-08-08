package com.example.hanaro.global.error;

import com.example.hanaro.global.payload.response.ApiResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400: @RequestBody @Valid JSON 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Map<String, String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        var ec = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponseDto.fail(ec.getStatus().value(), ec.getMessage(), errors));
    }

    // 400: @ModelAttribute 바인딩 실패
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponseDto<Map<String, String>>> handleBindException(BindException e) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));
        var ec = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponseDto.fail(ec.getStatus().value(), ec.getMessage(), errors));
    }

    // 400: @RequestParam / @PathVariable 검증 실패 (jakarta.validation.*)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<Map<String, String>>> handleConstraintViolation(ConstraintViolationException e) {
        Map<String, String> errors = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (a, b) -> a));
        var ec = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponseDto.fail(ec.getStatus().value(), ec.getMessage(), errors));
    }

    // 409: DB 유니크 제약 위반 (중복 키 등)
    //  - 하이버네이트 예외는 FQCN로 정확히 지정하여 jakarta.* 와 충돌 방지
    @ExceptionHandler({
            DataIntegrityViolationException.class,
            DuplicateKeyException.class,
            SQLIntegrityConstraintViolationException.class,
            org.hibernate.exception.ConstraintViolationException.class // ⬅️ 하이버네이트 DB 제약 위반
    })
    public ResponseEntity<ApiResponseDto<Void>> handleDbConstraint(RuntimeException e) {
        var ec = ErrorCode.EMAIL_ALREADY_EXISTS; // 일반화하려면 DUPLICATE_KEY 같은 코드로 변경
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponseDto.fail(ec.getStatus().value(), ec.getMessage()));
    }

    // 400: JSON 파싱/타입 오류 (잘못된 바디)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleNotReadable(HttpMessageNotReadableException e) {
        var ec = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponseDto.fail(ec.getStatus().value(), "요청 바디를 읽을 수 없습니다."));
    }

    // 400: 필수 파라미터 누락
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleMissingParam(MissingServletRequestParameterException e) {
        var ec = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponseDto.fail(ec.getStatus().value(), "필수 파라미터가 누락되었습니다: " + e.getParameterName()));
    }

    // 400: 타입 변환 실패 (?page=abc 같은 케이스)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        var ec = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponseDto.fail(ec.getStatus().value(), "파라미터 타입이 올바르지 않습니다: " + e.getName()));
    }

    // 도메인 비즈니스 예외 (상태코드는 ErrorCode에 따름)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleBusiness(BusinessException e) {
        var ec = e.getErrorCode();
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponseDto.fail(ec.getStatus().value(), ec.getMessage()));
    }

    // 405: 지원하지 않는 HTTP 메서드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(405)
                .body(ApiResponseDto.fail(405, "지원하지 않는 HTTP 메서드입니다."));
    }

    // 500: 마지막 안전망
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleEtc(Exception e) {
        log.error("Unhandled exception", e);
        var ec = ErrorCode.INTERNAL_ERROR;
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponseDto.fail(ec.getStatus().value(), ec.getMessage()));
    }
}
