package com.solbeg.userservice.mapper.service;

import com.solbeg.userservice.entity.Role;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenMapper {
    private final JwtTokenProvider jwtTokenProvider;

    @Named("createAccessToken")
    public String createAccessToken(User user) {
        return jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRoles());
    }

    @Named("createRefreshToken")
    public String createRefreshToken(User user) {
        return jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());
    }
}
