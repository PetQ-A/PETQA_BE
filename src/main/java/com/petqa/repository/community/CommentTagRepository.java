package com.petqa.repository.community;

import com.petqa.domain.CommentTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentTagRepository extends JpaRepository<CommentTag, Long> {
}
