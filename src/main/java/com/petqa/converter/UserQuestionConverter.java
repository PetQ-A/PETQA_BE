package com.petqa.converter;

import com.petqa.domain.Mapping.UserQuestion;
import com.petqa.dto.PagedResponseDTO;
import com.petqa.dto.QuestionDTO;
import org.springframework.data.domain.Page;

public class UserQuestionConverter {
    public static QuestionDTO.QuestionAnswerResopnseDTO toQuestionAnswerResopnseDTO(UserQuestion userQuestion) {
        return new QuestionDTO.QuestionAnswerResopnseDTO(
                userQuestion.getQuestion().getContent(),
                userQuestion.getAnswer().getId(),
                userQuestion.getAnswer().getAnswer(),
                userQuestion.getCreatedAt().toString(),
                userQuestion.getUpdatedAt().toString(),
                userQuestion.getQuestion().getId()
        );
    }

    public static QuestionDTO.QuestionUserResponseDTO toQuestionUserResponseDTO(UserQuestion userQuestion) {
        return new QuestionDTO.QuestionUserResponseDTO(
                userQuestion.getQuestion().getId(),
                userQuestion.getQuestion().getContent(),
                userQuestion.getAnswer().getId(),
                userQuestion.getCreatedAt(),
                userQuestion.getUpdatedAt()
        );
    }

    public static PagedResponseDTO<QuestionDTO.QuestionUserResponseDTO> toQuestionMemberResponseDTOPage(Page<UserQuestion> userQuestions) {
        return PagedResponseDTO.<QuestionDTO.QuestionUserResponseDTO>builder()
                .content(userQuestions.map(UserQuestionConverter::toQuestionUserResponseDTO).getContent())
                .totalPages(userQuestions.getTotalPages())
                .totalElements(userQuestions.getTotalElements())
                .first(userQuestions.isFirst())
                .last(userQuestions.isLast())
                .size(userQuestions.getSize())
                .numberOfElements(userQuestions.getNumberOfElements())
                .empty(userQuestions.isEmpty())
                .build();

    }
}
