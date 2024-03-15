package com.solbeg.userservice.service;

import com.solbeg.userservice.entity.UserToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserTokenService {

    UserToken createActivationToken(UUID userId);

    UserToken getByToken(String token);

    Page<UserToken> getAll(Pageable pageable);

    void deleteUserToken(String token);

    void deleteOldUserTokens();
}