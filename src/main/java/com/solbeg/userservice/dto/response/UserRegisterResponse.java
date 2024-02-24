package com.solbeg.userservice.dto.response;

import com.solbeg.userservice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private List<String> roles;
    private Status status;
}
