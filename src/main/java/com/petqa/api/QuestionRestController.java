package com.petqa.api;

import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.dto.PagedResponseDTO;
import com.petqa.dto.QuestionDTO;
import com.petqa.service.userQuestion.UserQuestionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionRestController {
    private final UserQuestionServiceImpl userQuestionServiceImpl;

    @GetMapping("/get")
    public ApiResponse<QuestionDTO.QuestionAnswerResopnseDTO> getQuestionAnswer(@RequestParam Long questionId, @RequestParam Long userId) {
        QuestionDTO.QuestionAnswerResopnseDTO responseDTO = userQuestionServiceImpl.getQuestionAnswer(questionId, userId);
        return ApiResponse.onSuccess(responseDTO);
    }

    @GetMapping("/user")
    public ApiResponse<PagedResponseDTO<QuestionDTO.QuestionUserResponseDTO>> getQuestionUser(
            @RequestParam Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDTO<QuestionDTO.QuestionUserResponseDTO> responseDTO = userQuestionServiceImpl.getQuestionUser(userId, pageable);
        return ApiResponse.onSuccess(responseDTO);
    }
}
