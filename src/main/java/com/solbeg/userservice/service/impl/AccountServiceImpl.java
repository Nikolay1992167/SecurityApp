package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.mapper.UserMapper;
import com.solbeg.userservice.service.AccountService;
import com.solbeg.userservice.service.UserIdentityService;
import com.solbeg.userservice.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final UserMapper userMapper;

    private final UserIdentityService userIdentityService;

    @Override
    public UserResponse getUserData() {
        User user = userIdentityService.getUserOrThrowException(AuthUtil.getUserId());
        return userMapper.toResponse(user);
    }
}
