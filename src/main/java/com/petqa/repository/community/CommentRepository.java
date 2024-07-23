package com.petqa.repository.community;

import com.petqa.domain.Comment;
import com.petqa.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByPost(Post post);
}
