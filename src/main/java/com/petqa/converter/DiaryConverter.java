package com.petqa.converter;

import com.petqa.domain.Diary;
import com.petqa.dto.diary.DiaryResponseDTO;

public class DiaryConverter {
    public static DiaryResponseDTO responseDTO(Diary diary){
        return DiaryResponseDTO.builder()
                .img(diary.getImg())
                .memo(diary.getMemo())
                .createdAt(diary.getCreatedAt())
                .modifiedAt(diary.getModifiedAt())
                .date(diary.getDate())
                .build();
    }
}
