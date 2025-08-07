package com.example.hanaro.domain.user.dto;

import com.example.hanaro.domain.user.entity.User;
import com.example.hanaro.domain.user.entity.User.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserJoinRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .role(Role.USER)
                .build();
    }
}
