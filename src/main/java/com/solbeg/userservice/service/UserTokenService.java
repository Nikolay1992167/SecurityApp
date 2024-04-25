package com.solbeg.userservice.service;

import com.solbeg.userservice.dto.response.UserTokenResponse;
import com.solbeg.userservice.entity.UserToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserTokenService {
    UserToken createActivationToken(UUID userId);

    UserToken getByToken(String token);

    Page<UserTokenResponse> getAllUserTokens(Pageable pageable);

    void deleteUserTokenByToken(String token);

    void deleteOldUserTokens();
}