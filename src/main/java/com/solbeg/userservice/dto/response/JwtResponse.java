package com.solbeg.userservice.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class JwtResponse {
    private UUID id;
    private String email;
    private String accessToken;
    private String refreshToken;
}
