package com.solbeg.userservice.dto.response;

import com.solbeg.userservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private UUID id;
    private UUID createdBy;
    private UUID updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private Role role;
    private boolean statusActive;
    private String token;
    private String tokenExpiration;
}
