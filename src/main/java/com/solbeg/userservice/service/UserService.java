package com.solbeg.userservice.service;


import com.solbeg.userservice.dto.request.UserAuthenticationRequest;
import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.DeleteResponse;
import com.solbeg.userservice.dto.response.TokenValidationResponse;
import com.solbeg.userservice.dto.response.UserResponse;

public interface UserService {

    UserResponse register(UserRegisterRequest request);

    UserResponse authenticate(UserAuthenticationRequest request);

    TokenValidationResponse tokenValidationCheck(String token);

    UserResponse updateByToken(UserUpdateRequest request, String token);

    DeleteResponse deleteByToken(String token);
}