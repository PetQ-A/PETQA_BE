package com.petqa.repository.community;

import com.petqa.domain.Comment;
import com.petqa.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByPost(Post post);


    @Query("SELECT c FROM Comment c WHERE "
            + "(:post IS NULL OR c.post = :post) AND "
            + "(:lastComment IS NULL OR c.id < :lastComment) AND"
            + "(c.parent IS NULL)"
            + "ORDER BY c.id DESC")
    List<Comment> findCommentsLatest(@Param("post") Post post,
                                     @Param("lastComment") Long lastComment,
                                     Pageable pageable);

    Optional<Comment> findByIdAndPostAndParentIsNull(Long commentId, Post post);


    @Query("SELECT c FROM Comment c JOIN FETCH c.tag WHERE "
            + "(c.parent = :comment) AND "
            + "(:lastReply IS NULL)"
            + "ORDER BY c.id ASC")
    List<Comment> findRepliesLatest(@Param("comment") Comment comment,
                                     @Param("lastReply") Long lastReply,
                                     Pageable pageable);
}
