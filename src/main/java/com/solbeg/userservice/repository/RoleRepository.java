package com.solbeg.userservice.repository;

import com.solbeg.userservice.entity.Role;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends BaseJpaRepository<Role, UUID> {

    Role findByName(String name);
}