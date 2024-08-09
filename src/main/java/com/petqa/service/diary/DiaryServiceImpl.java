package com.petqa.service.diary;

import com.petqa.converter.DiaryConverter;
import com.petqa.domain.Diary;
import com.petqa.domain.User;
import com.petqa.dto.diary.DiaryRequestDTO;
import com.petqa.dto.diary.DiaryResponseDTO;
import com.petqa.repository.DiaryRepository;
import com.petqa.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService{

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public DiaryResponseDTO writeMyDiary(String username, LocalDate diarydate, DiaryRequestDTO.AllDto request) {

//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("유저 없음"));
//
//        // DiaryRequestDTO.AllDto를 Diary 엔티티로 변환
//        Diary diary = request.toEntity(user);
//
//        // Diary 엔티티 저장
//        Diary savedDiary = diaryRepository.save(diary);
//
//        DiaryResponseDTO responseDTO = DiaryConverter.responseDTO(savedDiary);
//        return responseDTO;

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("유저 없음"));

            // DiaryRequestDTO.AllDto를 Diary 엔티티로 변환
            Diary diary = request.toEntity(user, diarydate);

            // Diary 엔티티 저장
            Diary savedDiary = diaryRepository.save(diary);

            // 관련된 User 엔티티를 병합
            userRepository.save(user);

            DiaryResponseDTO responseDTO = DiaryConverter.responseDTO(savedDiary);
            return responseDTO;

    }

    @Transactional
    @Override
    public DiaryResponseDTO modifyMyMemo(String username, LocalDate diaryDate, DiaryRequestDTO.MemoDto request){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유저 없음"));
        // 다이어리를 diaryDate로 찾기
        Diary diary = diaryRepository.findByDate(diaryDate)
                .orElseThrow(() -> new RuntimeException("다이어리 없음"));

        System.out.println("User: " + user);
        System.out.println("Diary User: " + diary.getUser());

        // 사용자가 다이어리의 작성자인지 확인
        if (!diary.getUser().equals(user)) { throw new RuntimeException("수정 권한 없음");}

        // 다이어리의 메모 업데이트
        diary.setMemo(request.getMemo());

        // 다이어리 저장
        Diary updatedDiary = diaryRepository.save(diary);

        DiaryResponseDTO responseDTO = DiaryConverter.responseDTO(updatedDiary);
        return responseDTO;
    }

    @Transactional
    @Override
    public DiaryResponseDTO modifyMyImage(String username, LocalDate diaryDate, DiaryRequestDTO.ImageDto request){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        Diary diary = diaryRepository.findByDate(diaryDate)
                .orElseThrow(() -> new RuntimeException("다이어리 없음"));

        if (!diary.getUser().equals(user)) { throw new RuntimeException("수정 권한 없음"); }

        diary.setMemo(request.getImg());
        Diary updatedDiary = diaryRepository.save(diary);

        DiaryResponseDTO responseDTO = DiaryConverter.responseDTO(updatedDiary);
        return responseDTO;
    }

    @Transactional
    @Override
    public DiaryResponseDTO eraseMyImage(String username, LocalDate diaryDate){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        Diary diary = diaryRepository.findByDate(diaryDate)
                .orElseThrow(() -> new RuntimeException("다이어리 없음"));

        if (!diary.getUser().equals(user)) { throw new RuntimeException("수정 권한 없음"); }

        String imageUrl = diary.getImg();
        if (imageUrl==null) { throw new RuntimeException("삭제할 이미지가 없습니다.");}

        diary.setImg(null);
        Diary erasedImg = diaryRepository.save(diary);

        DiaryResponseDTO responseDTO = DiaryConverter.responseDTO(erasedImg);
        return responseDTO;
    }

    @Override
    public DiaryResponseDTO showMyDiary(String username, LocalDate diaryDate){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        Diary diary = diaryRepository.findByUserAndDate(user, diaryDate)
                .orElseThrow(() -> new RuntimeException("해당 날짜에 다이어리 없음"));

        DiaryResponseDTO responseDTO = DiaryConverter.responseDTO(diary);
        return responseDTO;
    }

    @Override
    public List<DiaryResponseDTO> showMyAllDiary(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        List<Diary> diaries = diaryRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("해당 날짜에 다이어리 없음"));

        return diaries.stream()
                .map(DiaryConverter::responseDTO)
                .collect(Collectors.toList());
    }
}


