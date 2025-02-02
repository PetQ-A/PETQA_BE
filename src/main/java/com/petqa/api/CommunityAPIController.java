package com.petqa.api;

import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.dto.community.CommunityRequestDTO;
import com.petqa.dto.community.CommunityResponseDTO;
import com.petqa.service.community.CommunityCommandService;
import com.petqa.service.community.CommunityQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
@Slf4j
public class CommunityAPIController {

    private final CommunityQueryService communityQueryService;
    private final CommunityCommandService communityCommandService;


    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<CommunityResponseDTO.PostListResponseDTO>>> postList(@RequestParam(defaultValue = "전체") String category,
                                                                                                @RequestParam(defaultValue = "전체") String region,
                                                                                                @RequestParam(defaultValue = "최신순") String sort,
                                                                                                @RequestParam(required = false) String keyword,
                                                                                                @RequestParam(defaultValue = "20") Integer size,
                                                                                                @RequestParam(required = false) Long lastPost,
                                                                                                @RequestParam(required = false) Long lastView) {


        return ResponseEntity.ok(ApiResponse.onSuccess(communityQueryService.getPostList(category, region, sort, keyword, size, lastPost, lastView)));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<CommunityResponseDTO.PostResponseDTO>> postRead(@PathVariable Long postId) {

        communityCommandService.upView(postId);
        return ResponseEntity.ok(ApiResponse.onSuccess(communityQueryService.getPost(postId)));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommunityResponseDTO.CommentResponseDTO>>> commentRead(@RequestParam(required = false) Integer size,
                                                                                                  @RequestParam(required = false) Long lastComment,
                                                                                                  @PathVariable Long postId) {

        return ResponseEntity.ok(ApiResponse.onSuccess(communityQueryService.getComment(size, lastComment, postId)));
    }

    @GetMapping("/{postId}/comments/{commentId}/replies")
    public ResponseEntity<ApiResponse<List<CommunityResponseDTO.ReplyResponseDTO>>> replyRead(@RequestParam(required = false) Integer size,
                                                                                              @RequestParam(required = false) Long lastReply,
                                                                                              @PathVariable Long postId,
                                                                                              @PathVariable Long commentId) {

        return ResponseEntity.ok(ApiResponse.onSuccess(communityQueryService.getReply(size, lastReply, postId, commentId)));
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<String>> createPost(@RequestPart(name = "files", required = false) List<MultipartFile> files,
                                                          @RequestPart(name = "dto") CommunityRequestDTO.PostCreateRequestDTO postCreateRequestDTO) {

        communityCommandService.createPost(files, postCreateRequestDTO);

        return ResponseEntity.ok(ApiResponse.onSuccess("게시글 생성"));
    }


    @PostMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<String>> createComment(@PathVariable Long postId,
                                                             @RequestBody CommunityRequestDTO.CommentCreateRequestDTO commentCreateRequestDTO) {

        communityCommandService.createComment(commentCreateRequestDTO, postId);

        return ResponseEntity.ok(ApiResponse.onSuccess("댓글 생성"));
    }

    @PostMapping("/{postId}/vote")
    public ResponseEntity<ApiResponse<String>> vote(@PathVariable Long postId,
                                                    @RequestBody CommunityRequestDTO.VoteRequestDTO voteRequestDTO) {

        communityCommandService.vote(voteRequestDTO, postId);

        return ResponseEntity.ok(ApiResponse.onSuccess("투표 완료"));
    }

    @PostMapping("/{postId}/comments/{commentId}/replies")
    public ResponseEntity<ApiResponse<String>> createReply(@PathVariable Long postId,
                                                           @PathVariable Long commentId,
                                                           @RequestBody CommunityRequestDTO.ReplyCreateRequestDTO replyCreateRequestDTO) {

        communityCommandService.createReply(replyCreateRequestDTO, postId, commentId);

        return ResponseEntity.ok(ApiResponse.onSuccess("답글 생성"));
    }
}
