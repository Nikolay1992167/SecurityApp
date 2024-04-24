package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.response.UserTokenResponse;
import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.enums.TokenType;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.mapper.UserTokenMapper;
import com.solbeg.userservice.repository.UserTokenRepository;
import com.solbeg.userservice.service.UserIdentityService;
import com.solbeg.userservice.service.UserService;
import com.solbeg.userservice.service.UserTokenService;
import com.solbeg.userservice.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {
    private final UserTokenRepository tokenRepository;
    private final UserIdentityService userIdentityService;
    private final UserTokenMapper userTokenMapper = Mappers.getMapper(UserTokenMapper.class);

    @Override
    @Transactional
    public UserToken createActivationToken(UUID userId) {
        UserToken userToken = UserToken.builder()
                .user(userIdentityService.getUserOrThrowException(userId))
                .expirationAt(LocalDateTime.now().plusDays(3))
                .token(UUID.randomUUID().toString())
                .tokenType(TokenType.ACTIVATION)
                .build();
        log.info("IN createActivationToken - userToken: {}", userToken);
        return tokenRepository.persist(userToken);
    }

    @Override
    public UserToken getByToken(String token) {
        UserToken userToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USERTOKEN_NOT_FOUND.getMessage() + token));
        log.info("IN findByToken - userToken: {} found by token: {}", userToken, token);
        return userToken;
    }

    @Override
    public Page<UserTokenResponse> getAllUserTokens(Pageable pageable) {
        Page<UserToken> response = tokenRepository.findAll(pageable);
        log.info("IN findAll - {} usertokens found", response.stream().count());
        return response
                .map(userTokenMapper::toUserTokenResponse);
    }

    @Override
    @Transactional
    public void deleteUserTokenByToken(String token) {
        tokenRepository.findByToken(token).ifPresentOrElse(
                activationLink -> tokenRepository.deleteByToken(token),
                () -> {
                    throw new NotFoundException(ErrorMessage.USERTOKEN_NOT_FOUND.getMessage() + token);
                }
        );
        log.info("IN deleteUserToken - deleted UserToken with token {}", token);
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 7 * 60 * 60 * 1000)
    public void deleteOldUserTokens() {
        tokenRepository.deleteByExpirationAtBefore(LocalDateTime.now());
        log.info("IN deleteOldUserTokens - the method worked");
    }
}