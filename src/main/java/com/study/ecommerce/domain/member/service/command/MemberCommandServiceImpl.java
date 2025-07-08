package com.study.ecommerce.domain.member.service.command;

import com.study.ecommerce.domain.member.entity.Member;
import com.study.ecommerce.domain.member.repository.MemberRepository;
import com.study.ecommerce.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService{
    private final MemberRepository memberRepository;

    @Override
    public Member createMember(String email, String name, String password) {
        return createMember(email, name, password, null, Member.Role.CUSTOMER);
    }

    @Override
    public Member createMember(String email, String name, String password, String address, Member.Role role) {
        Member member = Member.builder()
                .email(email)
                .name(name)
                .password(password)
                .address(address)
                .role(role)
                .build();
        return memberRepository.save(member);
    }

    @Override
    public Member updateMember(Long memberId, String name) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        member.updateName(name);
        return memberRepository.save(member);
    }

    @Override
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        memberRepository.delete(member);
    }
}