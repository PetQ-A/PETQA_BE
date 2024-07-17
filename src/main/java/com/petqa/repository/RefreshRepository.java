package com.petqa.repository;

import com.petqa.domain.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RefreshRepository extends JpaRepository<Refresh, Long> {
    Boolean existsByRefresh(String refresh);

    void deleteByRefresh(String refresh);

}
