package com.example.hanaro.domain.user.service;

import com.example.hanaro.domain.user.dto.UserJoinRequest;
import com.example.hanaro.domain.user.entity.User;
import com.example.hanaro.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(UserJoinRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // DTO → 엔티티로 변환 후 저장
        User user = request.toEntity(encodedPassword);
        User savedUser = userRepository.save(user);

        return savedUser.getId(); // 가입된 사용자 ID 반환
    }
}
