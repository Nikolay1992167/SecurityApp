package com.solbeg.userservice.controller;

import com.solbeg.userservice.controller.openapi.UserOpenApi;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.mapper.UserMapper;
import com.solbeg.userservice.service.UserIdentityService;
import com.solbeg.userservice.service.UserService;
import com.solbeg.userservice.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('JOURNALIST') || hasAuthority('SUBSCRIBER')")
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
public class UserController implements UserOpenApi {
    private final UserIdentityService userIdentityService;
    private final UserMapper userMapper;

    @Override
    @GetMapping("/details")
    public UserResponse getUserData() {
        User user = userIdentityService.getUserOrThrowException(AuthUtil.getUserId());
        return userMapper.toResponse(user);
    }
}