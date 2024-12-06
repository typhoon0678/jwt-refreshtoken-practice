package com.example.jwt_refresh_practice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt_refresh_practice.dto.SignUpDTO;
import com.example.jwt_refresh_practice.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public String signUp(SignUpDTO signUpDTO) {
        memberService.signUpProcess(signUpDTO);
        return "ok";
    }
}
