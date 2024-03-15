package com.solbeg.userservice.controller;

import com.solbeg.userservice.controller.openapi.UserOpenApi;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.service.UserService;
import com.solbeg.userservice.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping(value = "/api/v1/users", produces = "application/json")
public class UserController implements UserOpenApi {

    private final UserService userService;
    private final UserTokenService tokenService;

    @Override
    @GetMapping("/tokens")
    public ResponseEntity<Page<UserToken>> findAllUserTokens(@PageableDefault(15) Pageable pageable) {
        Page<UserToken> userTokens = tokenService.getAll(pageable);
        return ResponseEntity.ok(userTokens);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<UserResponse>> findAll(@PageableDefault(15) Pageable pageable) {
        Page<UserResponse> usersPage = userService.findAll(pageable);
        return ResponseEntity.ok(usersPage);
    }

    @Override
    @GetMapping("/{uuid}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(userService.findUserById(uuid));
    }

    @Override
    @PutMapping("/{uuid}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID uuid, @Validated @RequestBody UserUpdateRequest updateRequest) {
        return ResponseEntity.ok(userService.update(uuid, updateRequest));
    }

    @Override
    @PatchMapping("/activation")
    public ResponseEntity<?> activateUserJournalist(@RequestParam String userToken, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        userService.activateJournalistAccount(userToken, token);
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable UUID id, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        userService.deactivateUser(id, token);
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        userService.deleteUser(id, token);
        return ResponseEntity.ok().build();
    }
}