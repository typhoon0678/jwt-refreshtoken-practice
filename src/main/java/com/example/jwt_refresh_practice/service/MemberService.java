package com.example.jwt_refresh_practice.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jwt_refresh_practice.dto.SignUpDTO;
import com.example.jwt_refresh_practice.entity.Member;
import com.example.jwt_refresh_practice.entity.Refresh;
import com.example.jwt_refresh_practice.repository.MemberRepository;
import com.example.jwt_refresh_practice.repository.RefreshRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Value("${spring.jwt.expired.refresh}")
    private int refreshExpired;

    private final MemberRepository memberRepository;
    private final RefreshRepository refreshRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signUpProcess(SignUpDTO signUpDTO) {

        if (memberRepository.existsByUsername(signUpDTO.getUsername())) {
            return;
        }

        Member signUpMember = Member.builder()
                .username(signUpDTO.getUsername())
                .password(bCryptPasswordEncoder.encode(signUpDTO.getPassword()))
                .role("ROLE_USER")
                .build();

        memberRepository.save(signUpMember);
    }

    public boolean isRefreshTokenExists(String refresh) {
        return refreshRepository.existsByRefresh(refresh);
    }

    public void deleteRefresh(String refresh) {
        refreshRepository.deleteByRefresh(refresh);
    }

    public void saveRefresh(String username, String refresh) {

        Date date = new Date(System.currentTimeMillis() + refreshExpired * 1000L);

        Refresh newRefresh = Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(newRefresh);
    }
}
