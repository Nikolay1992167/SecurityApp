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
  //  UserResponse getUser();

    void registerJournalist(UserRegisterRequest request);

    void registerSubscriber(UserRegisterRequest request);

    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse findUserById(UUID userId);

    User findActiveUserByEmailOrThrowException(String userEmail);

    //User getUserOrThrowException(UUID userId);

    UserResponse updateUserById(UUID userId, UserUpdateRequest userUpdateRequest);

    void activateJournalistAccount(String userEmail);

    void deactivateUserById(UUID userId);

    void deleteUserById(UUID userId);
}