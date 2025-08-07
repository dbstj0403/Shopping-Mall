package com.example.hanaro.domain.user.repository;

import com.example.hanaro.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);       // 중복 체크용
    Optional<User> findByEmail(String email);  // 로그인 시 사용
}
