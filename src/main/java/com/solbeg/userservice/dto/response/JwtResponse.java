package com.solbeg.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private UUID id;
    private String email;
    private String accessToken;
    private String refreshToken;
}
