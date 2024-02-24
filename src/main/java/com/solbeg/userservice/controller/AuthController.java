package com.solbeg.userservice.controller;

import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.request.RefreshTokenRequest;
import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.dto.response.UserRegisterResponse;
import com.solbeg.userservice.service.AuthService;
import com.solbeg.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> authenticate(@Validated @RequestBody JwtRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/registerjournalist")
    public ResponseEntity<UserRegisterResponse> registerJournalist(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerJournalist(request));
    }

    @PostMapping("/registersubscriber")
    public ResponseEntity<UserRegisterResponse> registerSubscriber(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerSubscriber(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refresh(refreshTokenRequest.getRefreshToken()));
    }
}