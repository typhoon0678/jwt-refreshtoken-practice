package com.example.jwt_refresh_practice.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import com.example.jwt_refresh_practice.entity.Refresh;
import com.example.jwt_refresh_practice.repository.RefreshRepository;

@ExtendWith(MockitoExtension.class)
public class RefreshServiceTest {

    @Value("${spring.jwt.expired.refresh}")
    private int refreshExpired;

    @Mock
    private RefreshRepository refreshRepository;

    @InjectMocks
    private RefreshService refreshService;

    @BeforeEach
    public void createRefresh() {

        String username = "username1";
        String refresh = "refreshToken";
        Date date = new Date(System.currentTimeMillis() + refreshExpired * 1000L);

        Refresh refreshEntity = Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        when(refreshRepository.save(any(Refresh.class))).thenReturn(refreshEntity);

        refreshService.saveRefresh(username, refresh);
    }

    @Test
    @DisplayName("refreshToken이 생성되었는지 확인한다.")
    public void testSaveRefresh() {

        // given
        String refresh = "refreshToken";

        // when
        when(refreshRepository.existsByRefresh(refresh)).thenReturn(true);

        // then
        verify(refreshRepository).save(any(Refresh.class));
        assertTrue(refreshRepository.existsByRefresh(refresh));
    }

    @Test
    @DisplayName("refreshToken이 삭제되었는지 확인한다.")
    public void testDeleteRefresh() {

        // given
        String refresh = "refreshToken";

        // when
        refreshRepository.deleteByRefresh(refresh);

        // then
        verify(refreshRepository).deleteByRefresh(refresh);
    }

    @Test
    @DisplayName("refreshToken을 검색할 수 있는지 확인한다.")
    public void testIsRefreshTokenExists() {

        // given
        String refresh = "refreshToken";

        // when
        when(refreshService.isRefreshTokenExists(refresh)).thenReturn(true);
        when(refreshRepository.existsByRefresh(refresh)).thenReturn(true);

        boolean isExists = refreshService.isRefreshTokenExists(refresh);

        // then
        verify(refreshRepository).existsByRefresh(any(String.class));
        assertTrue(isExists);
    }
}
