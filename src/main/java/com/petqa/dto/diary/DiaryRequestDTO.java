package com.petqa.dto.diary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.petqa.domain.Diary;
import com.petqa.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
        private MultipartFile img; // 여기서 img 필드가 MultipartFile 타입이어야 함
        private String memo;

        public Diary toEntity(User user, LocalDate diaryDate, String imgUrl) {
            return Diary.builder()
                    .user(user)
                    .memo(memo)
                    .date(diaryDate)
                    .img(imgUrl)
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