package com.solbeg.userservice.controller;

import com.solbeg.userservice.controller.openapi.AdminOpenApi;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.dto.response.UserTokenResponse;
import com.solbeg.userservice.service.UserService;
import com.solbeg.userservice.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping(value = "/api/v1/admin", produces = "application/json")
@RequiredArgsConstructor
public class AdminController implements AdminOpenApi {
    private final UserService userService;
    private final UserTokenService tokenService;

    @Override
    @GetMapping("/tokens")
    public Page<UserTokenResponse> findAllUserTokens(Pageable pageable) {
        return tokenService.getAll(pageable);
    }

    @Override
    @GetMapping
    public Page<UserResponse> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @Override
    @GetMapping("/{uuid}")
    public UserResponse findById(@PathVariable UUID uuid) {
        return userService.findUserById(uuid);
    }

    @Override
    @PutMapping("/{uuid}")
    public UserResponse update(@PathVariable UUID uuid,
                               @Validated @RequestBody UserUpdateRequest updateRequest) {
        return userService.update(uuid, updateRequest);
    }

    @Override
    @PatchMapping("/activation")
    public void activateUserJournalist(@RequestParam String userToken,
                                       @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        userService.activateJournalistAccount(userToken, token);
    }

    @Override
    @PatchMapping("/deactivate/{id}")
    public void deactivateUser(@PathVariable UUID id,
                               @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        userService.deactivateUser(id, token);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable UUID id,
                           @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        userService.deleteUser(id, token);
    }
}