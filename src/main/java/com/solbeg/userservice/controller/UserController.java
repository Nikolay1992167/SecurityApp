package com.solbeg.userservice.controller;

import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = "application/json")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> findAll(@PageableDefault(15) Pageable pageable) {
        Page<UserResponse> usersPage = userService.findAll(pageable);
        return ResponseEntity.ok(usersPage);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(userService.findUserById(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID uuid, @RequestBody UserUpdateRequest updateRequest) {
        return ResponseEntity.ok(userService.update(uuid, updateRequest));
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable UUID id, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        userService.deactivateUser(id, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        userService.deleteUser(id, token);
        return ResponseEntity.ok().build();
    }
}