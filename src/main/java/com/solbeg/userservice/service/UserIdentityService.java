package com.solbeg.userservice.service;

import com.solbeg.userservice.entity.User;

import java.util.UUID;

public interface UserIdentityService {

    User getUserOrThrowException(UUID userId);
}