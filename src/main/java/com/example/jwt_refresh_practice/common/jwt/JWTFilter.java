package com.example.jwt_refresh_practice.common.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.jwt_refresh_practice.dto.CustomMemberDetails;
import com.example.jwt_refresh_practice.entity.Member;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (!jwtUtil.isValid(authorization)) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        Member loginMember = Member.builder()
                .username(jwtUtil.getUsername(token))
                .password("password")
                .role(jwtUtil.getRole(token))
                .build();

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(loginMember);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null,
                customMemberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

}
