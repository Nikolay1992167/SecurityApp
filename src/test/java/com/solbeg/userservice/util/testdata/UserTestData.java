package com.solbeg.userservice.util.testdata;

import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserRegisterResponse;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.Role;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.CREATED_AT_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.CREATED_BY_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.JOURNALIST_FIRST_NAME;
import static com.solbeg.userservice.util.initdata.InitData.JOURNALIST_LAST_NAME;
import static com.solbeg.userservice.util.initdata.InitData.JOURNALIST_LIST_OF_ROLES;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_AT_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_BY_JOURNALIST;

@Data
@Builder(setterPrefix = "with")
public class UserTestData {

    @Builder.Default
    private UUID id = ID_JOURNALIST;

    @Builder.Default
    private UUID createdBy = CREATED_BY_JOURNALIST;

    @Builder.Default
    private UUID updatedBy = UPDATED_BY_JOURNALIST;

    @Builder.Default
    private LocalDateTime createdAt = CREATED_AT_JOURNALIST;

    @Builder.Default
    private LocalDateTime updatedAt = UPDATED_AT_JOURNALIST;

    @Builder.Default
    private String firstName = JOURNALIST_FIRST_NAME;

    @Builder.Default
    private String lastName = JOURNALIST_LAST_NAME;

    @Builder.Default
    private String password = PASSWORD_JOURNALIST;

    @Builder.Default
    private String email = EMAIL_JOURNALIST;

    @Builder.Default
    private Status status = Status.ACTIVE;

    @Builder.Default
    private List<String> roles = JOURNALIST_LIST_OF_ROLES;

    @Builder.Default
    private List<Role> rolesUser = List.of(RoleTestData.builder()
            .build()
            .getEntity());

    public User getUser() {
        return User.builder()
                .id(id)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .email(email)
                .status(status)
                .roles(rolesUser)
                .build();
    }

    public UserRegisterRequest getUserRegisterRequest() {
        return new UserRegisterRequest(firstName, lastName, email, password, status);
    }

    public UserUpdateRequest getUserUpdateRequest() {
        return new UserUpdateRequest(firstName, lastName, email, password);
    }

    public UserResponse getUserResponse() {
        return new UserResponse(id, createdBy, updatedBy, createdAt, updatedAt, firstName, lastName, password, email, roles, status);
    }

    public UserRegisterResponse getUserRegisterResponse() {
        return new UserRegisterResponse(id, firstName, lastName, password, email, roles, status);
    }
}