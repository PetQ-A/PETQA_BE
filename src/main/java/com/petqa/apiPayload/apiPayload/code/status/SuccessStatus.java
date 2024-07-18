package com.petqa.apiPayload.apiPayload.code.status;

import com.petqa.apiPayload.apiPayload.code.BaseCode;
import com.petqa.apiPayload.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 일반적인 응답
    OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    CREATED(HttpStatus.CREATED, "COMMON201", "생성되었습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT, "COMMON204", "콘텐츠가 없습니다."),
    RESET_CONTENT(HttpStatus.RESET_CONTENT, "COMMON205", "콘텐츠가 리셋되었습니다."),
    PARTIAL_CONTENT(HttpStatus.PARTIAL_CONTENT, "COMMON206", "일부 콘텐츠가 리턴되었습니다."),
    MULTI_STATUS(HttpStatus.MULTI_STATUS, "COMMON207", "다중 상태입니다."),
    ALREADY_REPORTED(HttpStatus.ALREADY_REPORTED, "COMMON208", "이미 보고되었습니다."),
    IM_USED(HttpStatus.IM_USED, "COMMON226", "IM 사용됨");


    // 멤버 관련 응답

    // ~~~ 관련 응답

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
