package com.example.jwt_refresh_practice.common.jwt;

import java.io.IOException;

import org.springframework.web.filter.GenericFilterBean;

import com.example.jwt_refresh_practice.repository.RefreshRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String requestUri = req.getRequestURI();
        String requestMethod = req.getMethod();
        if (!requestUri.matches("^\\/logout$") || !requestMethod.equals("POST")) {

            chain.doFilter(req, res);
            return;
        }

        String refresh = jwtUtil.getRefreshToken(req.getCookies());
        if (!jwtUtil.isValid(refresh, "refresh") || !refreshRepository.existsByRefresh(refresh)) {

            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        refreshRepository.deleteByRefresh(refresh);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        res.addCookie(cookie);
        res.setStatus(HttpServletResponse.SC_OK);
    }

}
