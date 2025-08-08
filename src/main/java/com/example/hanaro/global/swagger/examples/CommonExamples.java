package com.example.hanaro.global.swagger.examples;

public class CommonExamples {
    public static final String BAD_REQUEST = """
      { "status": 400, "message": "요청 형식이 올바르지 않습니다.", "data": {} }
    """;
    public static final String UNAUTHORIZED = """
      { "status": 401, "message": "인증이 필요하거나 토큰이 유효하지 않습니다.", "data": {} }
    """;
    public static final String FORBIDDEN = """
      { "status": 403, "message": "해당 리소스에 대한 권한이 없습니다.", "data": {} }
    """;
    public static final String SERVER_ERROR = """
      { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "data": {} }
    """;
}
