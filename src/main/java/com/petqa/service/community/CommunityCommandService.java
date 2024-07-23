package com.petqa.service.community;

import com.petqa.repository.community.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommunityCommandService {

    final private PostRepository postRepository;

    /*
     * 조회수 증가
     */
    public void upView(Long postId) {
        postRepository.incrementViewCount(postId);
    }
}
