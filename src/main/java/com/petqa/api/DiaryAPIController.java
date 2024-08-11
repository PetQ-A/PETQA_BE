package com.petqa.api;

import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.dto.diary.DiaryRequestDTO;
import com.petqa.dto.diary.DiaryResponseDTO;
import com.petqa.service.diary.DiaryService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.Resolution;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryAPIController {
    private final DiaryService diaryService;


    @PostMapping("/{diaryDate}")
    public ResponseEntity<ApiResponse<DiaryResponseDTO>> writeDiary(Principal principal,
                                                                    @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate diaryDate,
                                                                    @RequestPart("request") DiaryRequestDTO.AllDto request,
                                                                    @RequestPart(value = "petProfileImage", required = false) MultipartFile diaryImage) {

        request.setImg(diaryImage);
        String username = principal.getName();
        DiaryResponseDTO written = diaryService.writeMyDiary(username, diaryDate, request);

        return ResponseEntity.ok(ApiResponse.onSuccess(written));
    }

    @PatchMapping("/memo/{diaryDate}")
    public ResponseEntity<ApiResponse<DiaryResponseDTO>> modifyMemo(Principal principal,
                                                                    @PathVariable LocalDate diaryDate,
                                                                    @RequestBody DiaryRequestDTO.MemoDto request){
        String username = principal.getName();
        DiaryResponseDTO modified = diaryService.modifyMyMemo(username, diaryDate, request);

        return ResponseEntity.ok(ApiResponse.onSuccess(modified));
    }

    @PatchMapping("/picture/{diaryDate}")
    public ResponseEntity<ApiResponse<DiaryResponseDTO>> modifyImage(Principal principal,
                                                                     @PathVariable LocalDate diaryDate,
                                                                     @RequestPart(value = "petProfileImage", required = false) MultipartFile diaryImage){
        String username = principal.getName();
        DiaryResponseDTO modified = diaryService.modifyMyImage(username, diaryDate, diaryImage);

        return ResponseEntity.ok(ApiResponse.onSuccess(modified));
    }

    @DeleteMapping("/picture/{diaryDate}")
    public ResponseEntity<ApiResponse<DiaryResponseDTO>> eraseImage(Principal principal,
                                                                    @PathVariable LocalDate diaryDate){
        String username = principal.getName();
        DiaryResponseDTO modified = diaryService.eraseMyImage(username, diaryDate);

        return ResponseEntity.ok(ApiResponse.onSuccess(modified));
    }

    @GetMapping("/{diaryDate}")
    public ResponseEntity<ApiResponse<DiaryResponseDTO>> showDiary(Principal principal,
                                                                   @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                   LocalDate diaryDate){
        String username = principal.getName();
        DiaryResponseDTO eachDiary = diaryService.showMyDiary(username, diaryDate);

        return ResponseEntity.ok(ApiResponse.onSuccess(eachDiary));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<DiaryResponseDTO>>> showAllDiary(Principal principal){
        String username = principal.getName();
        List<DiaryResponseDTO> allDiary = diaryService.showMyAllDiary(username);

        return ResponseEntity.ok(ApiResponse.onSuccess(allDiary));
    }

}
