package com.petqa.dto.mypage;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MypageRequestDTO {
    private String username;
    private String petType;
    private String petName;
    private LocalDate petBirth;
}
