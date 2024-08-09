package com.petqa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class QuestionDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionAnswerResopnseDTO{
        String question;
        Long answerId;
        String answer;
        String createdAt;
        String updatedAt;
        Long questionId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionUserResponseDTO {
        Long questionId;
        String questionContent;
        Long answerId;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }
}
