package com.petqa.api;

import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.dto.answer.AnswerDTO;
import com.petqa.service.service.AnswerService;
import com.petqa.service.service.AnswerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerRestController {
    private final AnswerServiceImpl answerServiceImpl;

    @PostMapping("/submit")
    public ApiResponse<String> submitAnswer(@RequestBody @Valid AnswerDTO.AnswerSubmitRequestDTO request) {

        if (answerServiceImpl.confirmUserQuestion(request)){
            answerServiceImpl.updateUserQuestion(request);
        }else{
            answerServiceImpl.addUserQuestion(request);
        }
        return ApiResponse.onSuccess("제출 성공");
    }


}
