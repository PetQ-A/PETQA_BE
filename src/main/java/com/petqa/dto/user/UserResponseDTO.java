package com.petqa.dto.user;

import com.petqa.domain.enums.Gender;
import com.petqa.domain.enums.PetType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class UserResponseDTO {

    public static class UserDetail {

        private int kakaoId;

        @NotBlank
        @Size(max = 50)
        private String username;

        @NotNull
        @PositiveOrZero
        private Integer points;

        @NotNull
        @PositiveOrZero
        private int questionCount;

        PetDetail petDetail;

    }

    public static class PetDetail {
        @NotNull
        private Long id;

        @NotBlank
        @Size(max = 50)
        private String name;

        @NotBlank
        @Size(max = 50)
        private String species;


        @NotNull
        private PetType petType;

        @NotNull
        @Past
        private LocalDate birth;

        private String profileImage;

        @Positive
        private double weight;

        @NotNull
        private Gender gender;
    }
}
