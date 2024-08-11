package com.petqa.repository;

import com.petqa.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Pet findByUserId(Long userId);
}
