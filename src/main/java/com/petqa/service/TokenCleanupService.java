package com.petqa.service;

import com.petqa.domain.Refresh;
import com.petqa.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupService {

    private final RefreshRepository refreshRepository;


    @Scheduled(cron = "0 0 2 * * ?") // 매일 새벽 2시에 실행
    public void cleanupExpiredTokens() {
        Date now = new Date();
        List<Refresh> expiredTokens = refreshRepository.findByExpirationBefore(now);
        refreshRepository.deleteAll(expiredTokens);
        log.info("Cleaned up {} expired tokens", expiredTokens.size());
    }
}