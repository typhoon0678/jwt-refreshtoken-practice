package com.example.jwt_refresh_practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.jwt_refresh_practice.entity.Refresh;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {
    
    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);
}
