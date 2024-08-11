package com.petqa.converter;

import com.petqa.domain.Pet;
import com.petqa.dto.user.UserResponseDTO;

public class MypageConverter {
    public static UserResponseDTO.PetDetail convertToPetDetail(Pet pet) {
        return UserResponseDTO.PetDetail.builder()
                .id(pet.getId())
                .name(pet.getName())
                .petType(pet.getPetType())
                .birth(pet.getBirth())
                .profileImage(pet.getProfileImage())
                .weight(pet.getWeight())
                .gender(pet.getGender())
                .build();
    }
}
