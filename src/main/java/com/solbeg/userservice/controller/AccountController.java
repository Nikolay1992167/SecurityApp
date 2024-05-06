package com.solbeg.userservice.controller;

import com.solbeg.annotation.Logging;
import com.solbeg.userservice.controller.openapi.AccountOpenApi;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Logging
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
public class AccountController implements AccountOpenApi {
    private final AccountService accountService;

    @Override
    @GetMapping("/details")
    public UserResponse getUserData() {
        return accountService.getUserData();
    }
}