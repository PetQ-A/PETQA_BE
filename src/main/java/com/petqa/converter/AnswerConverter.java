package com.petqa.converter;

import com.petqa.domain.Answer;
import com.petqa.domain.Question;
import com.petqa.domain.User;
import com.petqa.domain.Mapping.UserQuestion;
import com.petqa.dto.answer.AnswerDTO;

public class AnswerConverter {
    public static Answer toAnswer(AnswerDTO.AnswerSubmitRequestDTO request){
        return Answer.builder()
                .answer(request.getAnswerContent())
                .build();
    }

    public static UserQuestion toUserQuestion(AnswerDTO.AnswerSubmitRequestDTO request, User user, Question question, Answer answer){
        return UserQuestion.builder()
                .user(user)
                .answer(answer)
                .question(question)
                .build();
    }
}
