package com.solbeg.userservice.repository;

import com.solbeg.userservice.entity.User;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseJpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            INNER JOIN UserToken ut
            ON u.id = ut.createdBy
            WHERE ut.token = :token
            """)
    Optional<User> findByUserWhenRegistrationWithToken(String token);
}