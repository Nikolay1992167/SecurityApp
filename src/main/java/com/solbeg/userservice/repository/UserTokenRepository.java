package com.solbeg.userservice.repository;

import com.solbeg.userservice.entity.UserToken;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserTokenRepository extends BaseJpaRepository<UserToken, UUID> {

    Optional<UserToken> findByToken(String token);

    Page<UserToken> findAll(Pageable pageable);

    void deleteByToken(String token);

    void deleteByExpirationAtBefore(LocalDateTime controlTime);
}
