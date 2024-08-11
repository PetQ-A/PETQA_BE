package com.petqa.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AnswerDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerSubmitRequestDTO{
        private Long questionId;

        private String answerContent;

        private Long userId;
    }
}
