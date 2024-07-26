package com.petqa.service.refresh;

import com.petqa.domain.Refresh;
import com.petqa.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RefreshCommandServiceImpl implements RefreshCommandService {

    private final RefreshRepository refreshRepository;

    @Override
    public void addRefresh(String username, String socialId, String refreshToken, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refresh = Refresh.builder()
                .socialId(socialId)
                .username(username)
                .refresh(refreshToken)
                .expiration(date)
                .build();

        log.info("Saving refresh token for user: {}, socialId: {}", username, socialId);
        Refresh savedRefresh = refreshRepository.save(refresh);
        log.info("Refresh token saved successfully. ID: {}", savedRefresh.getId());

    }
}
