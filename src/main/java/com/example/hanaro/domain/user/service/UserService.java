package com.example.hanaro.domain.user.service;

import com.example.hanaro.domain.user.dto.UserResponseDto;
import com.example.hanaro.domain.user.dto.JoinRequestDto;
import com.example.hanaro.domain.user.entity.User;
import com.example.hanaro.domain.user.repository.UserRepository;
import com.example.hanaro.global.error.CustomException;
import com.example.hanaro.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(JoinRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS); // 409
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = request.toEntity(encodedPassword);
        return userRepository.save(user).getId();
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponseDto(u.getId(), u.getName(), u.getEmail()))
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 404
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND); // 404
        }
        userRepository.deleteById(id);
    }
}
