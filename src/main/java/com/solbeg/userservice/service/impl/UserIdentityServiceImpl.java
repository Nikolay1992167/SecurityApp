package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.repository.UserRepository;
import com.solbeg.userservice.service.UserIdentityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserIdentityServiceImpl implements UserIdentityService {
    private final UserRepository userRepository;

    @Override
    public User getUserOrThrowException(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + userId));
    }
}