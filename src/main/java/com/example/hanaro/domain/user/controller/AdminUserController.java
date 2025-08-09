package com.example.hanaro.domain.user.controller;

import com.example.hanaro.domain.user.dto.UserResponseDto;
import com.example.hanaro.domain.user.service.UserService;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "USER API", description = "어드민 전용 회원 관리 API입니다.")
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @Operation(summary = "회원 목록 조회", description = "전체 회원 목록을 조회합니다.")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponseDto.ok(users));
    }

    @Operation(summary = "회원 삭제", description = "회원 ID로 회원을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}

