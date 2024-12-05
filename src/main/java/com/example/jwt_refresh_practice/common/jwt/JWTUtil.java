package com.example.jwt_refresh_practice.common.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.jwt_refresh_practice.entity.Refresh;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;

@Component
public class JWTUtil {

    @Value("${spring.jwt.expired.access}")
    private int accessExpired;
    @Value("${spring.jwt.expired.refresh}")
    private int refreshExpired;

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username",
                String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role",
                String.class);
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category",
                String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration()
                .before(new Date());
    }

    public Boolean isValid(String token, String category) {
        if (token == null || isExpired(token) || !getCategory(token).equals(category)) {
            return false;
        }

        return true;
    }

    public String createJwt(String category, String username, String role) {

        Long expiredMs = 0L;
        if (category.equals("access"))
            expiredMs = accessExpired * 1000L;
        if (category.equals("refresh"))
            expiredMs = refreshExpired * 1000L;

        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public Cookie createCookie(String category, String token) {

        int maxAge = 0;
        if (category.equals("access"))
            maxAge = accessExpired;
        if (category.equals("refresh"))
            maxAge = refreshExpired;

        Cookie cookie = new Cookie(category, token);
        cookie.setMaxAge(maxAge);
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public String getRefreshToken(Cookie[] cookies) {

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                return cookie.getValue();
            }
        }

        return null;
    }

    public Refresh createNewRefresh(String username, String refresh) {

        Date date = new Date(System.currentTimeMillis() + refreshExpired * 1000L);

        return Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();
    }
}
