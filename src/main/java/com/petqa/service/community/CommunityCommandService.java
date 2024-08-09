package com.petqa.service.community;

import com.petqa.apiPayload.apiPayload.code.status.ErrorStatus;
import com.petqa.apiPayload.apiPayload.exception.handler.CommunityHandler;
import com.petqa.apiPayload.apiPayload.exception.handler.FileUploadHandler;
import com.petqa.apiPayload.apiPayload.exception.handler.UserHandler;
import com.petqa.base.Util;
import com.petqa.domain.*;
import com.petqa.domain.enums.Category;
import com.petqa.domain.enums.Region;
import com.petqa.dto.community.CommunityRequestDTO;
import com.petqa.repository.UserRepository;
import com.petqa.repository.community.*;
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
    private final CommentRepository commentRepository;
    private final CommentTagRepository commentTagRepository;
    private final VoteCountRepository voteCountRepository;

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
                           CommunityRequestDTO.PostCreateRequestDTO postCreateRequestDTO) {

        log.info("게시글 생성");

        User user = Util.getCurrentUser();

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

            if (file.isEmpty()) {
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

        if (postCreateRequestDTO.getVote() != null) {
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

    public void createComment(CommunityRequestDTO.CommentCreateRequestDTO commentCreateRequestDTO,
                              Long postId) {

        User user = Util.getCurrentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommunityHandler(ErrorStatus.POST_NOT_EXIST));

        log.info("댓글 생성");
        commentRepository.save(Comment.builder()
                .content(commentCreateRequestDTO.getContent())
                .user(user)
                .post(post)
                .build());
    }

    public void createReply(CommunityRequestDTO.ReplyCreateRequestDTO replyCreateRequestDTO,
                            Long postId,
                            Long commentId) {

        User user = Util.getCurrentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommunityHandler(ErrorStatus.POST_NOT_EXIST));

        User tagUser = userRepository.findById(replyCreateRequestDTO.getTag().getUserId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommunityHandler(ErrorStatus.COMMENT_NOT_EXIST));

        log.info("답글 생성");
        // 답글 생성
        Comment reply = commentRepository.save(Comment.builder()
                .content(replyCreateRequestDTO.getContent())
                .parent(parentComment)
                .post(post)
                .user(user)
                .build());

        log.info("답글 태그 생성");
        // 답글 태그 생성
        commentTagRepository.save(CommentTag.builder()
                .comment(reply)
                .user(tagUser)
                .build());
    }

    public void vote(CommunityRequestDTO.VoteRequestDTO voteRequestDTO,
                           Long postId) {

        User user = Util.getCurrentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommunityHandler(ErrorStatus.POST_NOT_EXIST));

        Vote vote = voteRepository.findByPost(post)
                .orElseThrow(() -> new CommunityHandler(ErrorStatus.VOTE_NOT_EXIST));

        // 복수선택 체크
        // 복수 선택인데 multi가 false이면 예외처리
        if (voteRequestDTO.getItem().size() > 1 && !vote.getMulti()) {
            throw new CommunityHandler(ErrorStatus.VOTE_NOT_MULTI);
        }


        // end 기간 확인
        // 투표한 datetime이 end보다 이후이면 예외처리
        if (LocalDateTime.now().isAfter(vote.getEnd())) {
            throw new CommunityHandler(ErrorStatus.VOTE_AFTER_END);
        }

        // 투표했는지 안했는지 확인
        if (voteCountRepository.existsByUserAndVote(user, vote)) {
            throw new CommunityHandler(ErrorStatus.VOTE_COUNT_ALREADY_EXIST);
        }


        // VoteCount 생성
        // VoteItem number 증가
        voteRequestDTO.getItem()
                .forEach(item -> {

                    VoteItem voteItem = voteItemRepository.findById(item.getVoteItemId())
                            .orElseThrow(() -> new CommunityHandler(ErrorStatus.VOTE_ITEM_NOT_EXIST));

                    if(!voteItem.getVote().equals(vote)) {
                        throw new CommunityHandler(ErrorStatus.VOTE_ITEM_NOT_EXIST);
                    }

                    voteItemRepository.incrementNumberCount(voteItem.getId());

                    voteCountRepository.save(VoteCount.builder()
                            .voteItem(voteItem)
                            .user(user)
                            .build());
                });
    }
}
