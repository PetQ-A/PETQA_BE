package com.petqa.repository;

import com.petqa.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserBySocialIdAndUsername(String socialId, String username);

    @Query("select u from User u join fetch u.pet where u.socialId = :socialId and u.username = :username")
    Optional<User> findUserAndPetBySocialIdAndUsername(@Param("socialId") String socialId, @Param("username") String username);
}
