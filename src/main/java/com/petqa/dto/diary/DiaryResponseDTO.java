package com.petqa.dto.diary;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
public class DiaryResponseDTO {
    String img;
    String memo;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
    LocalDate date; // 이름

}
