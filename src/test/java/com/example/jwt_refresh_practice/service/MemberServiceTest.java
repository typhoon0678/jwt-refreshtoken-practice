package com.example.jwt_refresh_practice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.jwt_refresh_practice.dto.SignUpDTO;
import com.example.jwt_refresh_practice.entity.Member;
import com.example.jwt_refresh_practice.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입을 완료했을 때, 해당 멤버를 찾을 수 있는지 확인한다.")
    public void testSignUpProcess() {

        // given
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUsername("username1");
        signUpDTO.setPassword("password");

        Member member = Member.builder()
                .username(signUpDTO.getUsername())
                .password(bCryptPasswordEncoder.encode(signUpDTO.getPassword()))
                .role("ROLE_USER")
                .build();

        // when
        when(bCryptPasswordEncoder.encode(signUpDTO.getPassword())).thenReturn("hashedPassword");
        when(memberRepository.existsByUsername(signUpDTO.getUsername())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(memberRepository.findByUsername(signUpDTO.getUsername())).thenReturn(Optional.of(member));

        memberService.signUpProcess(signUpDTO);

        // then
        verify(memberRepository).save(any(Member.class));
        assertThat(memberRepository.findByUsername(signUpDTO.getUsername()).get().getUsername()).isEqualTo("username1");
    }

    @Test
    @DisplayName("중복된 username의 경우 회원가입이 되지 않는다.")
    public void testSignUpProcessFali() {

        // given
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUsername("username1");
        signUpDTO.setPassword("password");

        // when
        when(memberRepository.existsByUsername(signUpDTO.getUsername())).thenReturn(true);
        
        memberService.signUpProcess(signUpDTO);

        // then
        verify(memberRepository, never()).save(any(Member.class));
    }
}
