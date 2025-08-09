package com.example.hanaro.domain.user.controller;

import com.example.hanaro.global.config.jwt.JwtTokenProvider;
import com.example.hanaro.domain.user.dto.LoginRequestDto;
import com.example.hanaro.domain.user.dto.TokenResponseDto;
import com.example.hanaro.global.error.ErrorCode;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Tag(name = "USER API", description = "회원 관련 API입니다.")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "로그인", description = "JWT 토큰 기반 로그인 API입니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<TokenResponseDto>> login(@RequestBody LoginRequestDto req) {
        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

            String role = auth.getAuthorities().iterator().next().getAuthority();
            String token = jwtTokenProvider.generate(auth.getName(), role);

            return ResponseEntity.ok(ApiResponseDto.ok(new TokenResponseDto(token)));

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            return ResponseEntity
                    .status(ErrorCode.BAD_CREDENTIALS.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.BAD_CREDENTIALS));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(ErrorCode.INTERNAL_ERROR.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INTERNAL_ERROR, e.getMessage()));
        }
    }
}
