package com.petqa.repository.community;

import com.petqa.domain.User;
import com.petqa.domain.Vote;
import com.petqa.domain.VoteCount;
import com.petqa.domain.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteCountRepository extends JpaRepository<VoteCount, Long> {

    Boolean existsByUserAndVote(User user, Vote vote);

    Long countByVoteItem(VoteItem voteItem);

    Long countByVote(Vote vote);
}
