package com.petqa.repository;

import com.petqa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MypageRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}
