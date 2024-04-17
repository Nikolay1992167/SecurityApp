package com.solbeg.userservice.controller;

import com.solbeg.userservice.controller.openapi.UserOpenApi;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = "application/json")
public class UserController implements UserOpenApi {
    private final UserService userService;

    @Override
    @PostMapping("/details")
    public UserResponse getUserData(@RequestBody String token) {
        return userService.findUserByToken(token);
    }
}