package com.petqa.converter;

import com.petqa.domain.Mapping.UserQuestion;
import com.petqa.domain.Pet;
import com.petqa.domain.Question;
import com.petqa.domain.User;
import com.petqa.dto.mapping.MainPageDTO;
import com.petqa.repository.PetRepository;
import com.petqa.repository.QuestionRepository;
import com.petqa.repository.UserQuestionRepository;
import com.petqa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

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

        Optional<UserQuestion> optionalUserQuestion = userQuestionRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
        //일단 유저-질문 테이블에서 해당 유저의 가장 최근 값을 가져오고

        Question todayQuestion;
        boolean questionStatus = false;

        if (optionalUserQuestion.isPresent()) {
            UserQuestion userQuestion = optionalUserQuestion.get();
            todayQuestion = questionRepository.findById((userQuestion.getQuestion().getId()))
                    .orElse(null);

            LocalDate today = LocalDate.now();
            questionStatus = user.getUserQuestionsList().stream()
                    .anyMatch(uq -> uq.getCreatedAt().toLocalDate().equals(today));
        }else {
            todayQuestion = questionRepository.findById(Long.valueOf(1))
                    .orElseThrow(()->new IllegalArgumentException("Default question not found"));
        }

        return MainPageDTO.MainPageResponseDTO.builder()
                .petName(pet.getName())
                .petCategory(pet.getPetType().name())
                .point(Long.valueOf((user.getPoints())))
                .todayQuestion(todayQuestion.getContent())
                .questionStatus(questionStatus)
                .build();
    }
}
