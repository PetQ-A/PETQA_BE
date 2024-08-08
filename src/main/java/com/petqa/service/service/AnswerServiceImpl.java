package com.petqa.service.service;

import com.petqa.converter.AnswerConverter;
import com.petqa.domain.Answer;
import com.petqa.domain.Question;
import com.petqa.domain.User;
import com.petqa.domain.mapping.UserQuestion;
import com.petqa.dto.answer.AnswerDTO;
import com.petqa.repository.AnswerRepository;
import com.petqa.repository.QuestionRepository;
import com.petqa.repository.UserQuestionRepository;
import com.petqa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService{
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserQuestionRepository userQuestionRepository;

    @Override
    @Transactional
    public Answer addAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    @Override
    @Transactional
    public UserQuestion addUserQuestion(AnswerDTO.AnswerSubmitRequestDTO request) {
        User user =userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question Not Found"));
        Answer answer = AnswerConverter.toAnswer(request);

        Answer savedAnswer = answerRepository.save(answer);

        if (user.getQuestionCount() == Math.toIntExact(request.getQuestionId())){
            user.incrementQuestionCount();
            userRepository.save(user);
        }

        UserQuestion userQuestion = AnswerConverter.toUserQuestion(request, user, question, answer);
        return userQuestionRepository.save(userQuestion);
    }

    @Override
    @Transactional
    public UserQuestion updateUserQuestion(AnswerDTO.AnswerSubmitRequestDTO request){
        UserQuestion userQuestion =userQuestionRepository.findByQuestionIdAndUserId(request.getQuestionId(), request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("UserQuestion Not Found"));

        Answer answer = AnswerConverter.toAnswer(request);
        Answer savedAnswer = answerRepository.save(answer);

        userQuestion.updateAnswer(savedAnswer);// 명시적 메서드를 통해 Answer 업데이트
        userQuestion.updateTimestamp(); // 타임스탬프 업데이트

        return userQuestionRepository.save(userQuestion);
    }

    @Override
    @Transactional
    public boolean confirmUserQuestion(AnswerDTO.AnswerSubmitRequestDTO request){
        Long questionId = request.getQuestionId();
        Long userId = request.getUserId();

        return userQuestionRepository.existsByQuestionIdAndUserId(questionId, userId);
    }
}
