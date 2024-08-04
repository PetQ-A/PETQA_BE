package com.petqa.api;

import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.dto.community.CommunityResponseDTO;
import com.petqa.service.community.CommunityCommandService;
import com.petqa.service.community.CommunityQueryService;
import com.petqa.service.s3Bucket.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityAPIController {

    private final CommunityQueryService communityQueryService;
    private final CommunityCommandService communityCommandService;
    private final S3Service s3Service;


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
}
