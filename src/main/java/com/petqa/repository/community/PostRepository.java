package com.petqa.repository.community;

import com.petqa.domain.Post;
import com.petqa.domain.enums.Category;
import com.petqa.domain.enums.Region;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE "
            + "(:category IS NULL OR p.category = :category) AND "
            + "(:region IS NULL OR p.region = :region) AND "
            + "(:keyword IS NULL OR p.title LIKE %:keyword%) AND "
            + "(:lastPost IS NULL OR p.id < :lastPost) "
            + "ORDER BY p.id DESC")
    List<Post> findPostsLatest(@Param("category") Category category,
                               @Param("region") Region region,
                               @Param("keyword") String keyword,
                               @Param("lastPost") Long lastPost,
                               Pageable pageable);

    @Query("SELECT p FROM Post p WHERE "
            + "(:category IS NULL OR p.category = :category) AND "
            + "(:region IS NULL OR p.region = :region) AND "
            + "(:keyword IS NULL OR p.title LIKE %:keyword%) AND "
            + "(:lastView IS NULL OR p.view < :lastView) "
            + "ORDER BY p.view DESC")
    List<Post> findPostsPopular(@Param("category") Category category,
                               @Param("region") Region region,
                               @Param("keyword") String keyword,
                               @Param("lastView") Long lastView,
                               Pageable pageable);

    @Modifying
    @Query("UPDATE Post p SET p.view = p.view + 1 WHERE p.id = :postId")
    void incrementViewCount(Long postId);
}
