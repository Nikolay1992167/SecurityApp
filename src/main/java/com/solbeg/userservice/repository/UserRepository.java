package com.solbeg.userservice.repository;

import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.Status;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseJpaRepository<User, UUID> {
   // @EntityGraph("roles")
    Optional<User> findByEmailAndStatus(String email, Status status);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @EntityGraph("roles")
    Optional<User> findUserFetchRolesById(UUID userId);
}