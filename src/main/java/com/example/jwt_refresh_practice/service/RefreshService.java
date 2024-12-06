package com.example.jwt_refresh_practice.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.jwt_refresh_practice.entity.Refresh;
import com.example.jwt_refresh_practice.repository.RefreshRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshService {
    
    @Value("${spring.jwt.expired.refresh}")
    private int refreshExpired;
    
    private final RefreshRepository refreshRepository;
    
    public Refresh saveRefresh(String username, String refresh) {

        Date date = new Date(System.currentTimeMillis() + refreshExpired * 1000L);

        Refresh newRefresh = Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        return refreshRepository.save(newRefresh);
    }

    public void deleteRefresh(String refresh) {
        refreshRepository.deleteByRefresh(refresh);
    }
    
    public boolean isRefreshTokenExists(String refresh) {
        return refreshRepository.existsByRefresh(refresh);
    }
}
