package com.petqa.service.userQuestion;

import com.petqa.converter.UserQuestionConverter;
import com.petqa.domain.Question;
import com.petqa.domain.Mapping.UserQuestion;
import com.petqa.dto.PagedResponseDTO;
import com.petqa.dto.QuestionDTO;
import com.petqa.repository.UserQuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserQuestionServiceImpl implements UserService{
    private final UserQuestionRepository userQuestionRepository;

    public QuestionDTO.QuestionAnswerResopnseDTO getQuestionAnswer (Long questionId, Long userId) {
        UserQuestion userQuestion = userQuestionRepository.findByQuestionIdAndUserId(questionId, userId)
                .orElseThrow(() ->new IllegalArgumentException("주어진 질문 번호와 유저 번호로 답변을 조회할 수 없습니다."));
        return UserQuestionConverter.toQuestionAnswerResopnseDTO(userQuestion);
    }

    public PagedResponseDTO<QuestionDTO.QuestionUserResponseDTO> getQuestionUser(Long userId, Pageable pageable) {
        Page<UserQuestion> userQuestions = userQuestionRepository.findByUserIdOrderByQuestionIdAsc(userId, pageable);

        return UserQuestionConverter.toQuestionMemberResponseDTOPage(userQuestions);
    }
}
