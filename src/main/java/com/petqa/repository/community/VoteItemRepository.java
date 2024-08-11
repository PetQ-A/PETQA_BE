package com.petqa.repository.community;

import com.petqa.domain.Vote;
import com.petqa.domain.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteItemRepository extends JpaRepository<VoteItem, Long> {
    List<VoteItem> findByVote(Vote vote);

    @Modifying
    @Query("UPDATE VoteItem v SET v.number = v.number + 1 WHERE v.id = :voteItemId")
    void incrementNumberCount(Long voteItemId);
}
