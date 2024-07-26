package com.petqa.dto.user;

import com.petqa.domain.enums.Gender;
import com.petqa.domain.enums.PetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class UserRequestDTO {

    @Data
    public static class CreateUserDTO {
        @NotNull(message = "반려동물 종류는 필수값입니다.")
        private PetType petType;

        @NotBlank(message = "소셜 ID는 필수값입니다.")
        private String socialId;

        @NotBlank(message = "사용자 닉네임은 필수값입니다.")
        private String username;

        @NotBlank(message = "반려동물 이름은 필수값입니다.")
        private String petName;

        @NotBlank(message = "반려동물의 생일은 필수값입니다.")
        private LocalDate petBirthday;

        @NotNull(message = "반려동물의 성별은 필수값입니다.")
        private Gender petGender;

        private Double petWeight;

        private MultipartFile petProfileImage;

    }

    @Getter
    public static class CheckUsernameDuplicatedDTO {
        @NotEmpty(message = "사용자 닉네임은 필수값입니다.")
        private String username;
    }
}
