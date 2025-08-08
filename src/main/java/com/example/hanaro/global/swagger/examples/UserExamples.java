package com.example.hanaro.global.swagger.examples;

public final class UserExamples {
    private UserExamples() {}

    public static final String JOIN_SUCCESS = """
        { "status": 200, "message": "회원가입이 성공적으로 완료되었습니다! 🎉", "data": { "userId": 1 } }
    """;

    public static final String JOIN_VALIDATION_ERROR = """
        { "status": 400, "message": "잘못된 요청입니다.", "data": { "password": "비밀번호는 8자 이상 20자 이하이어야 합니다.", "name": "닉네임은 2자 이상 10자 이하이어야 합니다." } }
    """;

    public static final String JOIN_EMAIL_DUP = """
        { "status": 409, "message": "이미 사용 중인 이메일입니다.", "data": null }
    """;
}
