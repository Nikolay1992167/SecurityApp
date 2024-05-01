package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.mapper.UserMapper;
import com.solbeg.userservice.security.jwt.JwtTokenProvider;
import com.solbeg.userservice.service.AuthService;
import com.solbeg.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(final JwtRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()));

        User userInDB = userService.findActiveUserByEmailOrThrowException(loginRequest.getEmail());

        return userMapper.toJwtResponse(userInDB);
    }

    @Override
    public JwtResponse getRefreshToken(String refreshToken) {
        return jwtTokenProvider.refreshUserToken(refreshToken);
    }
}