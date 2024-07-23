package com.petqa.service.community;

import com.petqa.domain.Post;
import com.petqa.domain.PostImage;
import com.petqa.domain.Vote;
import com.petqa.domain.enums.Category;
import com.petqa.domain.enums.Region;
import com.petqa.dto.community.CommunityResponseDTO;
import com.petqa.repository.community.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CommunityQueryService {

    final private PostRepository postRepository;
    final private PostImageRepository postImageRepository;
    final private CommentRepository commentRepository;
    final private VoteRepository voteRepository;
    final private VoteItemRepository voteItemRepository;

    /*
     * 게시글 리스트 조회
     */
    public List<CommunityResponseDTO.PostListResponseDTO> getPostList(String category, String region, String sort,
                                                                      String keyword, Integer size, Long lastPost,
                                                                      Long lastView) {
        log.info("카테고리: {}, 지역: {}, 정렬: {}, 키워드: {}, 크기: {}, lastPost: {}, lastView: {}",
                category, region, sort, keyword, size, lastPost, lastView);

        Category resultCategory = category.equals("전체") ? null:Category.fromName(category);
        Region resultRegion = region.equals("전체") ? null:Region.fromName(region);

        PageRequest pageRequest = PageRequest.of(0, size != null ? size : 20);

        List<Post> postList;
        switch (sort) {
            case "최신순":
                postList = postRepository.findPostsLatest(resultCategory, resultRegion, keyword, lastPost, pageRequest);
                break;
            case "인기순":
                postList = postRepository.findPostsPopular(resultCategory, resultRegion, keyword, lastView, pageRequest);
                break;
            default:
                postList = postRepository.findPostsLatest(resultCategory, resultRegion, keyword, lastPost, pageRequest);
                break;
        }

        return postList.stream()
                .map(post -> CommunityResponseDTO.PostListResponseDTO.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .image(postImageRepository.findFirstByPost(post)
                                .map(PostImage::getImage)
                                .orElse(null))
                        .relativeTime(getRelativeTime(post.getCreatedAt()))
                        .view(post.getView())
                        .pet(CommunityResponseDTO.PostListResponseDTO.Pet.builder()
                                .name(post.getUser().getPet().getName())
                                .weight(post.getUser().getPet().getWeight())
                                .species(post.getUser().getPet().getSpecies())
                                .build())
                        .build())
                .toList();
    }


    /*
     * 게시글 조회
     */
    public CommunityResponseDTO.PostResponseDTO getPost(Long postId) {
        log.info("게시글 id: {} 조회", postId);

        Post post = postRepository.findById(postId).orElseThrow();

        Vote vote = voteRepository.findByPost(post).orElse(null);

        return CommunityResponseDTO.PostResponseDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .image(postImageRepository.findByPost(post).stream()
                        .map(PostImage::getImage)
                        .toList())
                .relativeTime(getRelativeTime(post.getCreatedAt()))
                .view(post.getView())
                .comment(commentRepository.countByPost(post))
                .pet(CommunityResponseDTO.PostResponseDTO.Pet.builder()
                        .name(post.getUser().getPet().getName())
                        .species(post.getUser().getPet().getSpecies())
                        .weight(post.getUser().getPet().getWeight())
                        .build())
                .user(CommunityResponseDTO.PostResponseDTO.User.builder()
                        .userId(post.getUser().getId())
                        .nickname(post.getUser().getUsername())
                        .build())
                .vote(vote != null ?
                        CommunityResponseDTO.PostResponseDTO.Vote.builder()
                                .multi(vote.getMulti())
                                .item(voteItemRepository.findByVote(vote).stream()
                                        .map(voteItem -> CommunityResponseDTO.PostResponseDTO.Vote.Item.builder()
                                                .voteItemId(voteItem.getId())
                                                .content(voteItem.getContent())
                                                .build())
                                        .toList())
                                .build()
                        : null)
                .build();
    }

    /*
     * 상대 시간 구하는 메서드
     */
    public static String getRelativeTime(LocalDateTime createdAt) {
        log.info("상대 시간 계산");

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // 두 시간 간의 차이
        Duration duration = Duration.between(createdAt, now);

        // 시간 차이를 초, 분, 시간 단위로 변환
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days/7;

        // 상대적인 시간 문자열 생성
        if (weeks > 0) {
            return weeks + "주 전";
        } else if (days > 0) {
            return days + "일 전";
        } else if (hours > 0) {
            return hours + "시간 전";
        } else if (minutes > 0) {
            return minutes + "분 전";
        } else {
            return "방금 전";
        }
    }
}
