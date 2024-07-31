package com.petqa.converter;

import com.petqa.domain.Mapping.UserQuestion;
import com.petqa.domain.Pet;
import com.petqa.domain.Question;
import com.petqa.domain.User;
import com.petqa.dto.mainpage.MainPageDTO;
import com.petqa.repository.PetRepository;
import com.petqa.repository.QuestionRepository;
import com.petqa.repository.UserQuestionRepository;
import com.petqa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MainPageConverter {
    private final UserQuestionRepository userQuestionRepository;
    private final PetRepository petRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public MainPageDTO.MainPageResponseDTO tomainPageResponseDTO(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        Pet pet = petRepository.findByUserId(userId);
        if (pet == null) {//조회했는데 pet이 null일 때,
            throw new IllegalArgumentException("Pet not found");
        }

        UserQuestion userQuestion = userQuestionRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserQuestion not found"));

        Question todayQuestion = questionRepository.findById(Long.valueOf(userQuestion.getUser().getQuestionCount()))
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));
        LocalDate today = LocalDate.now();

        boolean questionStatus = user.getUserQuestionsList().stream()
                .anyMatch(mq -> mq.getCreatedAt().toLocalDate().equals(today));

        return MainPageDTO.MainPageResponseDTO.builder()
                .petName(pet.getName())
                .petCategory(pet.getPetType().name())
                .point(Long.valueOf((user.getPoints())))
                .todayQuestion(todayQuestion.getContent())
                .questionStatus(questionStatus)
                .build();
    }
}
