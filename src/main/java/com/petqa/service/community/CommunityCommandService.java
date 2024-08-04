package com.petqa.service.community;

import com.petqa.apiPayload.apiPayload.code.status.ErrorStatus;
import com.petqa.apiPayload.apiPayload.exception.handler.FileUploadHandler;
import com.petqa.apiPayload.apiPayload.exception.handler.UserHandler;
import com.petqa.domain.*;
import com.petqa.domain.enums.Category;
import com.petqa.domain.enums.Region;
import com.petqa.dto.community.CommunityRequestDTO;
import com.petqa.repository.UserRepository;
import com.petqa.repository.community.PostImageRepository;
import com.petqa.repository.community.PostRepository;
import com.petqa.repository.community.VoteItemRepository;
import com.petqa.repository.community.VoteRepository;
import com.petqa.security.jwt.JwtUtil;
import com.petqa.service.s3Bucket.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommunityCommandService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;
    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;

    private final JwtUtil jwtUtil;

    private final S3Service s3Service;

    /*
     * 조회수 증가
     */
    public void upView(Long postId) {
        postRepository.incrementViewCount(postId);
    }


    /*
     * 게시글 작성
     */
    public void createPost(List<MultipartFile> files,
                           CommunityRequestDTO.PostCreateRequestDTO postCreateRequestDTO,
                           String refreshToken) {

        log.info("게시글 생성");

        String socialId = jwtUtil.getSocialId(refreshToken);
        String username = jwtUtil.getUsername(refreshToken);

        User user = userRepository.findUserBySocialIdAndUsername(socialId, username)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 게시글
        log.info("게시글 생성");
        Post post = Post.builder()
                .title(postCreateRequestDTO.getTitle())
                .category(Category.fromName(postCreateRequestDTO.getCategory()))
                .region(Region.fromName(postCreateRequestDTO.getRegion()))
                .content(postCreateRequestDTO.getContent())
                .view(0L)
                .user(user)
                .build();

        postRepository.save(post);

        // 게시글 사진
        log.info("게시글 사진 생성");
        for (MultipartFile file : files) {

            if(file.isEmpty()) {
                break;
            }

            String url;

            try {
                url = s3Service.uploadFile(file);
            } catch (IOException e) {
                throw new FileUploadHandler(ErrorStatus.FILE_UPLOAD_FAILED);
            }

            postImageRepository.save(PostImage.builder()
                    .image(url)
                    .post(post)
                    .build());
        }

        // 투표
        log.info("투표 생성");
        Vote vote = voteRepository.save(Vote.builder()
                .title(postCreateRequestDTO.getVote().getTitle())
                .multi(postCreateRequestDTO.getVote().getMulti())
                .end(LocalDateTime.now().plusDays(postCreateRequestDTO.getVote().getEnd()))
                .post(post)
                .build());

        // 투표 선택지
        log.info("투표 선택지 생성");
        for (String item : postCreateRequestDTO.getVote().getItems()) {
            voteItemRepository.save(VoteItem.builder()
                    .content(item)
                    .number(0L)
                    .vote(vote)
                    .build());
        }

    }
}
