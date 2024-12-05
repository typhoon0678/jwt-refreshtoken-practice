package com.example.jwt_refresh_practice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt_refresh_practice.common.jwt.JWTUtil;
import com.example.jwt_refresh_practice.dto.SignUpDTO;
import com.example.jwt_refresh_practice.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @PostMapping("/signup")
    public String signUp(SignUpDTO signUpDTO) {
        memberService.signUpProcess(signUpDTO);
        return "ok";
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = jwtUtil.getRefreshToken(request.getCookies());

        if (!jwtUtil.isValid(refresh, "refresh") || !memberService.isRefreshTokenExists(refresh)) {

            return new ResponseEntity<>("refresh token is not valid", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        String newAccess = jwtUtil.createJwt("access", username, role);
        String newRefresh = jwtUtil.createJwt("refresh", username, role);

        memberService.deleteRefresh(refresh);
        memberService.saveRefresh(username, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
