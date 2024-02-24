package com.solbeg.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$")
    private String refreshToken;
}
