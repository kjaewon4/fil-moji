package com.example.backend.service;

import com.example.backend.dto.MemberForm;
import com.example.backend.entity.Member;
import com.example.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(MemberForm form) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(form.getEmail())) {
            throw new IllegalStateException("이리 등록된 이메일입니다.");
        }

        // 비밀번호 해시 처리
        String encodedPassword = passwordEncoder.encode(form.getPassword());

        // DTO → Entity 변환
        Member member = new Member();
        member.setUsername(form.getUsername());
        member.setEmail(form.getEmail());
        member.setPassword(encodedPassword);

        memberRepository.save(member);
    }
}
