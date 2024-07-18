package com.petqa.repository;

import com.petqa.domain.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface RefreshRepository extends JpaRepository<Refresh, Long> {
    Boolean existsByRefresh(String refresh);

    void deleteByRefresh(String refresh);

    List<Refresh> findByExpirationBefore(Date now);
}
