package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.exception.NoSuchUserEmailException;
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

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(final JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()));
        User user = userService.findByUserEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NoSuchUserEmailException("User with email " + loginRequest.getEmail() + " is not exist"));

        jwtResponse.setId(user.getId());
        jwtResponse.setEmail(user.getEmail());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(
                user.getId(), user.getEmail(), user.getRoles()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(
                user.getId(), user.getEmail()));
        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserToken(refreshToken);
    }
}