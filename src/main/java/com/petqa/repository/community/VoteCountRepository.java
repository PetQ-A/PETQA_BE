package com.petqa.repository.community;

import com.petqa.domain.User;
import com.petqa.domain.VoteCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteCountRepository extends JpaRepository<VoteCount, Long> {

    Boolean existsByUser(User user);
}
