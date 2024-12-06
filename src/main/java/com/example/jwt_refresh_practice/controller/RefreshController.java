package com.example.jwt_refresh_practice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.jwt_refresh_practice.common.jwt.JWTUtil;
import com.example.jwt_refresh_practice.service.RefreshService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RefreshController {

    private final RefreshService refreshService;
    private final JWTUtil jwtUtil;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = jwtUtil.getRefreshToken(request.getCookies());

        if (!jwtUtil.isValid(refresh, "refresh") || !refreshService.isRefreshTokenExists(refresh)) {

            return new ResponseEntity<>("refresh token is not valid", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        String newAccess = jwtUtil.createJwt("access", username, role);
        String newRefresh = jwtUtil.createJwt("refresh", username, role);

        refreshService.deleteRefresh(refresh);
        refreshService.saveRefresh(username, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
