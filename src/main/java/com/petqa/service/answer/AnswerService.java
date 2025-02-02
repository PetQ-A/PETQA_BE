package com.petqa.service.answer;

import com.petqa.domain.Answer;
import com.petqa.domain.Mapping.UserQuestion;
import com.petqa.dto.answer.AnswerDTO;
import org.springframework.transaction.annotation.Transactional;

public interface AnswerService{
    @Transactional
    Answer addAnswer(Answer answer);

    @Transactional
    UserQuestion addUserQuestion(AnswerDTO.AnswerSubmitRequestDTO request);

    @Transactional
    UserQuestion updateUserQuestion(AnswerDTO.AnswerSubmitRequestDTO request);

    @Transactional
    boolean confirmUserQuestion(AnswerDTO.AnswerSubmitRequestDTO request);
}
