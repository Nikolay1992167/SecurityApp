package com.solbeg.userservice.controller;

import com.solbeg.userservice.dto.request.UserAuthenticationRequest;
import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.DeleteResponse;
import com.solbeg.userservice.dto.response.TokenValidationResponse;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth", produces = "application/json")
public class UserController {

    private final UserService userService;

    @PostMapping("/registerjournalist")
    public ResponseEntity<UserResponse> registerJournalist(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerJournalist(request));
    }

    @PostMapping("/registersubscriber")
    public ResponseEntity<UserResponse> registerSubscriber(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerSubscriber(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserResponse> authenticate(@RequestBody UserAuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> tokenValidationCheck(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                                        String token) {
        return ResponseEntity.ok(userService.tokenValidationCheck(token));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateByToken(@RequestBody UserUpdateRequest request,
                                                      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                      String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.updateByToken(request, token));
    }

    @DeleteMapping
    public ResponseEntity<DeleteResponse> deleteByToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                        String token) {
        return ResponseEntity.ok(userService.deleteByToken(token));
    }
}