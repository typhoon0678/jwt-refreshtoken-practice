package com.example.jwt_refresh_practice.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jwt_refresh_practice.dto.SignUpDTO;
import com.example.jwt_refresh_practice.entity.Member;
import com.example.jwt_refresh_practice.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member signUpProcess(SignUpDTO signUpDTO) {

        if (memberRepository.existsByUsername(signUpDTO.getUsername())) {
            return null;
        }

        Member signUpMember = Member.builder()
                .username(signUpDTO.getUsername())
                .password(bCryptPasswordEncoder.encode(signUpDTO.getPassword()))
                .role("ROLE_USER")
                .build();

        return memberRepository.save(signUpMember);
    }
}
