package com.petqa.repository;

import com.petqa.domain.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface RefreshRepository extends JpaRepository<Refresh, Long> {
    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);

    List<Refresh> findByExpirationBefore(Date now);

    List<Refresh> findRefreshTokenBySocialIdAndUsername(String socialId, String username);

    Optional<Refresh> findByRefresh(String refresh);
}
