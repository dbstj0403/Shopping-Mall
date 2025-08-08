package com.example.hanaro.global.swagger.examples;

public final class AdminExamples {
    private AdminExamples() {}

    public static final String GET_ALL_USERS_SUCCESS = """
      { "status": 200, "message": "성공",
        "data": [
          { "id": 1, "username": "별돌이", "email": "hanaro@email.com" },
          { "id": 2, "username": "별송이", "email": "hanaro2@email.com" }
        ]
      }
    """;

    public static final String DELETE_USER_SUCCESS = """
      { "status": 200, "message": "해당 회원이 성공적으로 삭제되었습니다.",
        "data": { "message": "해당 회원이 성공적으로 삭제되었습니다." }
      }
    """;
}
