package com.petqa.repository.community;

import com.petqa.domain.Post;
import com.petqa.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    Optional<PostImage> findFirstByPost(Post post);

    List<PostImage> findByPost(Post post);
}
