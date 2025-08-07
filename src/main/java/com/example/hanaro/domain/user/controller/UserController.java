package com.example.hanaro.domain.user.controller;

import com.example.hanaro.domain.user.dto.UserJoinRequest;
import com.example.hanaro.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "회원 관련 API입니다.")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody UserJoinRequest request) {
        Long userId = userService.join(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다. ID : " + userId);
    }
}
