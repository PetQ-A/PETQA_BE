package com.petqa.repository.community;

import com.petqa.domain.Vote;
import com.petqa.domain.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteItemRepository extends JpaRepository<VoteItem, Long> {
    List<VoteItem> findByVote(Vote vote);
}
