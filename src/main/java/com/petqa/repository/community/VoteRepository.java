package com.petqa.repository.community;

import com.petqa.domain.Post;
import com.petqa.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByPost(Post post);
}
