package com.example.hanaro.global.config.security;

import com.example.hanaro.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var u = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("not found"));
        var auth = new SimpleGrantedAuthority(u.getRole().name());

        return new CustomUserDetails(
                u.getId(),
                u.getEmail(),
                u.getPassword(),
                java.util.List.of(auth)
        );
    }
}
