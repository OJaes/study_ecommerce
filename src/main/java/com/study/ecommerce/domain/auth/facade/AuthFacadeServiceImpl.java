package com.study.ecommerce.domain.auth.facade;

import com.study.ecommerce.domain.auth.dto.req.LoginRequest;
import com.study.ecommerce.domain.auth.dto.req.SignUpRequest;
import com.study.ecommerce.domain.auth.dto.resp.TokenResponse;
import com.study.ecommerce.domain.auth.service.AuthCommandService;
import com.study.ecommerce.domain.member.entity.Member;
import com.study.ecommerce.domain.member.service.command.MemberCommandService;
import com.study.ecommerce.domain.member.service.query.MemberQueryService;
import com.study.ecommerce.global.error.ErrorCode;
import com.study.ecommerce.global.error.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacadeServiceImpl implements AuthFacadeService{
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final AuthCommandService authCommandService;

    @Override
    @Transactional
    public void signUp(SignUpRequest request) {
        if (memberQueryService.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }

        String encodePassword = authCommandService.encodePassword(request.password());

        Member member = memberCommandService.createMember(
                request.email(),
                request.name(),
                encodePassword,
                request.address(),
                Member.Role.CUSTOMER
        );
    }

    @Override
    public TokenResponse login(LoginRequest request) {

        // 회원 조회
        Member member = memberQueryService.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        // 비밀번호 검증
        if(!authCommandService.validatePassword(request.password(), member.getPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        // 토큰 생성
        // 회원정보로 토큰 응답 완성
        TokenResponse tokenResponse = authCommandService.generateToken(member.getEmail(), member.getRole().name());




        return new TokenResponse(
                tokenResponse.accessToken(),
                tokenResponse.tokenType(),
                member.getEmail(),
                member.getName(),
                member.getRole().toString()
        );
    }
}