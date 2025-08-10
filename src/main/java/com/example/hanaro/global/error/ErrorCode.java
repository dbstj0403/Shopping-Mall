package com.example.hanaro.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E001", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E002", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "E003", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E004", "대상을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E005", "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "E006", "이미 사용 중인 이메일입니다."),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "E007", "이메일 또는 비밀번호가 올바르지 않습니다."), // 추가
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E999", "서버 내부 오류가 발생했습니다."),
    CART_EMPTY(HttpStatus.BAD_REQUEST, "E008", "장바구니가 비어 있습니다."),
    OUT_OF_STOCK(HttpStatus.CONFLICT, "E009", "재고가 부족합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
