package com.solbeg.userservice.util.testdata;

import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
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
import static com.solbeg.userservice.util.initdata.InitData.CREATED_AT_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.CREATED_BY_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.CREATED_BY_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.FIRST_NAME_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.FIRST_NAME_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.JOURNALIST_LIST_OF_ROLES;
import static com.solbeg.userservice.util.initdata.InitData.LAST_NAME_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.LAST_NAME_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.ROLE_NAME_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_AT_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_AT_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_BY_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_BY_SUBSCRIBER;

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
    private String firstName = FIRST_NAME_JOURNALIST;

    @Builder.Default
    private String lastName = LAST_NAME_JOURNALIST;

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

    public User getJournalist() {
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

    public User getSubscriber() {
        return User.builder()
                .id(ID_SUBSCRIBER)
                .createdBy(CREATED_BY_SUBSCRIBER)
                .updatedBy(UPDATED_BY_SUBSCRIBER)
                .createdAt(CREATED_AT_SUBSCRIBER)
                .updatedAt(UPDATED_AT_SUBSCRIBER)
                .firstName(FIRST_NAME_SUBSCRIBER)
                .lastName(LAST_NAME_SUBSCRIBER)
                .password(PASSWORD_SUBSCRIBER)
                .email(EMAIL_SUBSCRIBER)
                .status(Status.ACTIVE)
                .roles(List.of(RoleTestData.builder()
                            .withName(ROLE_NAME_SUBSCRIBER)
                            .build()
                            .getEntity()))
                .build();
    }

    public UserRegisterRequest getRegisterRequestJournalist() {
        return UserRegisterRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();
    }

    public UserRegisterRequest getRegisterRequestSubscriber() {
        return UserRegisterRequest.builder()
                .firstName(FIRST_NAME_SUBSCRIBER)
                .lastName(LAST_NAME_SUBSCRIBER)
                .email(EMAIL_SUBSCRIBER)
                .password(PASSWORD_SUBSCRIBER)
                .build();
    }

    public UserUpdateRequest getUserUpdateRequest() {
        return UserUpdateRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();
    }

    public UserResponse getUserResponse() {
        return UserResponse.builder()
                .id(id)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .email(email)
                .roles(roles)
                .status(status)
                .build();
    }
}