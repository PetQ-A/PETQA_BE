package com.petqa.repository;

import com.petqa.domain.mapping.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    Optional<UserQuestion> findByQuestionIdAndUserId(Long questionId, Long userId);

    boolean existsByQuestionIdAndUserId(Long questionId, Long userId);
}
