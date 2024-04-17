package com.solbeg.userservice.controller;

import com.solbeg.userservice.controller.openapi.AuthOpenApi;
import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.request.RefreshTokenRequest;
import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.service.AuthService;
import com.solbeg.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = "application/json")
@RequiredArgsConstructor
public class AuthController implements AuthOpenApi {
    private final AuthService authService;
    private final UserService userService;

    @Override
    @PostMapping("/authenticate")
    public JwtResponse authenticate(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register/journalist")
    public void registerJournalist(@Validated @RequestBody UserRegisterRequest request) {
        userService.registerJournalist(request);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register/subscriber")
    public void registerSubscriber(@Validated @RequestBody UserRegisterRequest request) {
        userService.registerSubscriber(request);
    }

    @Override
    @PostMapping("/refresh")
    public JwtResponse refresh(@Validated @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refresh(refreshTokenRequest.getRefreshToken());
    }
}