package com.study.ecommerce.domain.auth.service;

import com.study.ecommerce.domain.auth.dto.resp.TokenResponse;
import com.study.ecommerce.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService{
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponse generateToken(String email, String role) {
        String token = jwtTokenProvider.createToken(
                email,
                Collections.singletonList(role)
        );
        return new TokenResponse(
                token,
                "Bearer",
                email,
                null,
                role
        );
    }

    @Override
    public void invalidateToken(String token) {
        // 실제로 redis나 db에서 블랙리스트 등록해야 함
        // 현재 구성한 jwt가 stateless 구현하지 않는 방향으로 진행함
    }

    @Override
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}