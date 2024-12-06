package com.example.jwt_refresh_practice.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.jwt_refresh_practice.common.jwt.JWTUtil;
import com.example.jwt_refresh_practice.entity.Refresh;

@DataJpaTest
@ComponentScan(basePackages = "com.example.jwt_refresh_practice.common.jwt")
@ActiveProfiles("test")
public class RefreshRepositoryTest {

    @Value("${spring.jwt.expired.access}")
    int refreshExpired;

    private final RefreshRepository refreshRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public RefreshRepositoryTest(RefreshRepository refreshRepository, JWTUtil jwtUtil) {
        this.refreshRepository = refreshRepository;
        this.jwtUtil = jwtUtil;
    }

    @BeforeEach
    public void createRefresh() {
        String refreshToken = jwtUtil.createJwt("refresh", "username1", "ROLE_USER");
        Date date = new Date(System.currentTimeMillis() + refreshExpired * 1000L);

        Refresh refresh = Refresh.builder()
                .username("username1")
                .refresh(refreshToken)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refresh);
    }

    @Test
    @DisplayName("리프레시 토큰을 저장하고, 해당 토큰을 불러올 수 있는지 확인한다.")
    public void testExistsByRefresh() {

        // given
        String refreshToken = jwtUtil.createJwt("refresh", "username1", "ROLE_USER");
        Date date = new Date(System.currentTimeMillis() + refreshExpired * 1000L);

        Refresh refresh = Refresh.builder()
                .username("testExistsByRefresh")
                .refresh(refreshToken)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refresh);

        // then
        assertTrue(refreshRepository.existsByRefresh(refreshToken));
        assertFalse(refreshRepository.existsByRefresh("testRefreshToken"));
    }

    @Test
    @DisplayName("리프레시 토큰을 저장하고 지웠을 때, 해당 토큰을 불러올 수 없는 것을 확인한다.")
    public void testDeleteByRefresh() {

        // given
        String refreshToken = jwtUtil.createJwt("refresh", "username1", "ROLE_USER");
        Date date = new Date(System.currentTimeMillis() + refreshExpired * 1000L);

        Refresh refresh = Refresh.builder()
                .username("testDeleteByRefresh")
                .refresh(refreshToken)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refresh);

        // when
        refreshRepository.deleteByRefresh(refreshToken);

        // then
        assertFalse(refreshRepository.existsByRefresh(refreshToken));
    }

}
