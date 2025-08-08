package com.example.hanaro.global.swagger.examples;

public final class CommonExamples {
    private CommonExamples() {}

    public static final String UNAUTHORIZED = """
      { "status": 401, "message": "인증이 필요합니다.", "data": null }
    """;

    public static final String FORBIDDEN = """
      { "status": 403, "message": "접근 권한이 없습니다.", "data": null }
    """;

    public static final String SERVER_ERROR = """
      { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "data": null }
    """;
}
