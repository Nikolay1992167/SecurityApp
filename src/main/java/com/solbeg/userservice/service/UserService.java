package com.solbeg.userservice.service;

import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    void registerJournalist(UserRegisterRequest request);

    void registerSubscriber(UserRegisterRequest request);

    Page<UserResponse> findAll(Pageable pageable);

    UserResponse findUserById(UUID uuid);

    Optional<User> findByUserEmail(String email);

    User findById(UUID id);

    UserResponse update(UUID uuid, UserUpdateRequest updateRequest);

    void activateJournalistAccount(String email, String token);

    void deactivateUser(UUID id, String token);

    void deleteUser(UUID id, String token);
}