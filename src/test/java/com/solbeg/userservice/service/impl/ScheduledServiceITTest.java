package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.repository.UserTokenRepository;
import com.solbeg.userservice.util.PostgresSqlContainerInitializer;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import static com.solbeg.userservice.util.initdata.InitData.DEFAULT_PAGE_REQUEST_FOR_IT;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ScheduledServiceITTest extends PostgresSqlContainerInitializer {
    private final UserTokenRepository userTokenRepository;
    private final ScheduledService scheduledService;

    @Test
    @Transactional
    void shouldDeleteOldUserTokens() {
        // given, when
        scheduledService.deleteOldUserTokens();

        // then
        Page<UserToken> userTokens = userTokenRepository.findAll(DEFAULT_PAGE_REQUEST_FOR_IT);
        assertThat(userTokens).isEmpty();
    }
}