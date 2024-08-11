package com.petqa.repository;

import com.petqa.domain.Mapping.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    Optional<UserQuestion> findTopByUserIdOrderByCreatedAtDesc(Long userId);

}
