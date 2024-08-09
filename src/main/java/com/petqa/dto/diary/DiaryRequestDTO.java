package com.petqa.dto.diary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.petqa.domain.Diary;
import com.petqa.domain.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.Date;


public class DiaryRequestDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AllDto {
        private String img;
        private String memo;

        public Diary toEntity(User user, LocalDate diarydate) {
            return Diary.builder()
                    .user(user)
                    .memo(memo)
                    .date(diarydate)
                    .img(img)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImageDto {
        private String img;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemoDto {
        private String memo;
    }
}