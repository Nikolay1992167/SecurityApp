package com.solbeg.userservice.mapper;

import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.Status;
import com.solbeg.userservice.mapper.service.JwtTokenMapper;
import com.solbeg.userservice.mapper.service.PasswordMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {PasswordMapper.class, JwtTokenMapper.class})
public interface UserMapper {

    @Mapping(target = "password", source = "source.password", qualifiedByName = "encodePassword")
    User fromRequest(UserRegisterRequest source, Status status);

    @Mapping(target = "updatedBy", source = "userId")
    void update(@MappingTarget User target, UserUpdateRequest source, UUID userId);

    UserResponse toResponse(User user);

    @Mapping(target = "accessToken", source = "user",  qualifiedByName = "createAccessToken")
    @Mapping(target = "refreshToken", source = "user", qualifiedByName = "createRefreshToken")
    JwtResponse toJwtResponse(User user);
}