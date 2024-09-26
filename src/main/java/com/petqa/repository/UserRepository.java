package com.petqa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petqa.domain.User;

import io.lettuce.core.dynamic.annotation.Param;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findUserBySocialIdAndUsername(String socialId, String username);

	@Query("select u from User u join fetch u.pet where u.socialId = :socialId and u.username = :username")
	Optional<User> findUserAndPetBySocialIdAndUsername(@Param("socialId") String socialId,
		@Param("username") String username);

	Optional<User> findByUsername(String username);

	Optional<User> findById(Long userId);

	Optional<User> findUserByUsername(String username);
}
