package com.example.hanaro.domain.user.controller;

import com.example.hanaro.config.jwt.JwtTokenProvider;
import com.example.hanaro.domain.user.dto.LoginRequest;
import com.example.hanaro.domain.user.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "USER API", description = "회원 관련 API입니다.")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "로그인", description = "JWT 토큰 기반 로그인 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 오류 혹은 유효성 검사 실패"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })

    @PostMapping("login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String email = auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        String token = jwtTokenProvider.generate(email, role);
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
