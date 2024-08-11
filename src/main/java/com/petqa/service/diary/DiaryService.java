package com.petqa.service.diary;

import com.petqa.dto.diary.DiaryRequestDTO;
import com.petqa.dto.diary.DiaryResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import java.util.Date;
import java.util.List;


public interface DiaryService {

    DiaryResponseDTO writeMyDiary(String username, LocalDate diarydate, DiaryRequestDTO.AllDto request);

    DiaryResponseDTO modifyMyMemo(String username, LocalDate diaryDate, DiaryRequestDTO.MemoDto request);

    DiaryResponseDTO modifyMyImage(String username, LocalDate diaryDate, MultipartFile diaryImage);

    DiaryResponseDTO eraseMyImage(String username, LocalDate diaryDate);

    DiaryResponseDTO showMyDiary(String username, LocalDate diaryDate);

    List<DiaryResponseDTO> showMyAllDiary(String username);

}
