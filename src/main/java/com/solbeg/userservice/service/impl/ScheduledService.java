package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledService {
    private final UserTokenRepository tokenRepository;

    @Transactional
    @Scheduled(fixedRate = 7 * 60 * 60 * 1000)
    public void deleteOldUserTokens() {
        tokenRepository.deleteByExpirationAtBefore(LocalDateTime.now());
        log.info("IN deleteOldUserTokens - the method worked");
    }
}
