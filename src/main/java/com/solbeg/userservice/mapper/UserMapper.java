package com.solbeg.userservice.mapper;

import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.Status;
import com.solbeg.userservice.mapper.service.JwtTokenMapper;
import com.solbeg.userservice.mapper.service.PasswordMapper;
import com.solbeg.userservice.mapper.service.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {RoleMapper.class, PasswordMapper.class, JwtTokenMapper.class})
public interface UserMapper {

    @Mapping(target = "roles", source = "roleName", qualifiedByName = "setRolesList")
    @Mapping(target = "password", source = "source.password", qualifiedByName = "encodePassword")
    User fromRequest(UserRegisterRequest source, Status status, String roleName);

    @Mapping(target = "updatedBy", source = "userId")
    @Mapping(target = "password", source = "source.password", qualifiedByName = "encodePassword")
    void update(@MappingTarget User target, UserUpdateRequest source, UUID userId);

    @Mapping(target = "password", source = "source.password", qualifiedByName = "encodePassword")
    UserResponse toResponse(User source);

    @Mapping(target = "accessToken", source = "user", qualifiedByName = "createAccessToken")
    @Mapping(target = "refreshToken", source = "user", qualifiedByName = "createRefreshToken")
    JwtResponse toJwtResponse(User user);
}