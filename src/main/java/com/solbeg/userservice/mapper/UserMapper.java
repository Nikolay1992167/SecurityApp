package com.solbeg.userservice.mapper;

import com.solbeg.userservice.dto.request.UserRegisterRequest;

import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserRegisterResponse;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.Role;
import com.solbeg.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {Role.class, Collectors.class})
public interface UserMapper {

    User fromRequest(UserRegisterRequest userRegisterRequest);

    User fromUpdateRequest(UserUpdateRequest updateRequest);

    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(Role::getName).toList())")
    UserRegisterResponse toRegisterResponse(User user);

    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(Role::getName).toList())")
    UserResponse toResponse(User user);
}