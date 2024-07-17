package com.petqa.repository;

import com.petqa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserBySocialIdAndUsername(String socialId, String username);

}
