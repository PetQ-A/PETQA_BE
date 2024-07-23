package com.petqa.repository;

import com.petqa.domain.Post;
import com.petqa.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    Optional<PostImage> findFirstByPost(Post post);
}
