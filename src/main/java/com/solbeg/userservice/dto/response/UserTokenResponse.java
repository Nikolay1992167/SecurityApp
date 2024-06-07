package com.solbeg.userservice.dto.response;

import com.solbeg.userservice.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenResponse implements Serializable {
    private UUID id;
    private LocalDateTime expirationAt;
    private String token;
    private TokenType tokenType;
    private UUID userId;
}