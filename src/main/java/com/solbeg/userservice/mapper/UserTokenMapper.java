package com.solbeg.userservice.mapper;

import com.solbeg.userservice.dto.response.UserTokenResponse;
import com.solbeg.userservice.entity.UserToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserTokenMapper {

    @Mapping(source = "user.id", target = "userId")
    UserTokenResponse toUserTokenResponse(UserToken userToken);
}