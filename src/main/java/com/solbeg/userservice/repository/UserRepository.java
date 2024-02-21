package com.solbeg.userservice.repository;

import com.solbeg.userservice.entity.User;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseJpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
}
