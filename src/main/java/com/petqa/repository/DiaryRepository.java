package com.petqa.repository;

import com.petqa.domain.Diary;
import com.petqa.domain.User;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Optional<Diary> findByDate(LocalDate diaryDate);

    Optional<Diary> findByUserAndDate(User user, LocalDate date);

    Optional<List<Diary>> findByUser(User user);
}
