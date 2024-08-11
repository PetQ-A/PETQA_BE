package com.petqa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.petqa.domain.Mapping.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    Optional<UserQuestion> findByQuestionIdAndUserId(Long questionId, Long userId);

    boolean existsByQuestionIdAndUserId(Long questionId, Long userId);
    Page<UserQuestion> findByUserIdOrderByQuestionIdAsc(Long userId, Pageable pageable); // QuestionId 기준으로 오름차순 정렬 및 페이징 처리
    Optional<UserQuestion> findTopByUserIdOrderByCreatedAtDesc(Long userId);

}
