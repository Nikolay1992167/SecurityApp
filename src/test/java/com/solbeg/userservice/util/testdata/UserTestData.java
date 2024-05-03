package com.solbeg.userservice.util.testdata;

import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

import static com.solbeg.userservice.util.initdata.InitData.CREATED_AT_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.CREATED_AT_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.CREATED_BY_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.CREATED_BY_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.FIRST_NAME_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.FIRST_NAME_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.FIRST_NAME_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.FIRST_NAME_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.ID_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.LAST_NAME_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.LAST_NAME_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.LAST_NAME_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.LAST_NAME_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_AT_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_AT_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_BY_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_BY_SUBSCRIBER;

public class UserTestData {

    public static User getJournalist() {
        return User.builder()
                .id(ID_JOURNALIST)
                .createdBy(CREATED_BY_JOURNALIST)
                .updatedBy(UPDATED_BY_JOURNALIST)
                .createdAt(CREATED_AT_JOURNALIST)
                .updatedAt(UPDATED_AT_JOURNALIST)
                .firstName(FIRST_NAME_JOURNALIST)
                .lastName(LAST_NAME_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .email(EMAIL_JOURNALIST)
                .status(Status.ACTIVE)
                .roles(List.of(RoleTestData.getJournalist()))
                .build();
    }

    public static User getJournalistWithStatusNotActive() {
        User user = getJournalist();
        user.setStatus(Status.NOT_ACTIVE);
        return user;
    }

    public static User getSubscriber() {
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
                .roles(List.of(RoleTestData.getSubscriber()))
                .build();
    }

    public static User getAdmin(){
        return  User.builder()
                .id(ID_ADMIN)
                .createdBy(null)
                .updatedBy(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .firstName(FIRST_NAME_ADMIN)
                .lastName(LAST_NAME_ADMIN)
                .password(PASSWORD_SUBSCRIBER)
                .email(EMAIL_ADMIN)
                .status(Status.ACTIVE)
                .roles(List.of(RoleTestData.getAdmin()))
                .build();
    }

    public static UserRegisterRequest getRegisterRequestJournalist() {
        return UserRegisterRequest.builder()
                .firstName(FIRST_NAME_JOURNALIST)
                .lastName(LAST_NAME_JOURNALIST)
                .email(EMAIL_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .build();
    }

    public static UserRegisterRequest getRegisterRequestJournalistWithIncorrectFirstName() {
        return UserRegisterRequest.builder()
                .firstName(FIRST_NAME_INCORRECT)
                .lastName(LAST_NAME_JOURNALIST)
                .email(EMAIL_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .build();
    }

    public static UserRegisterRequest getRegisterRequestJournalistWithIncorrectLastName() {
        return UserRegisterRequest.builder()
                .firstName(FIRST_NAME_JOURNALIST)
                .lastName(LAST_NAME_INCORRECT)
                .email(EMAIL_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .build();
    }

    public static UserRegisterRequest getRegisterRequestSubscriber() {
        return UserRegisterRequest.builder()
                .firstName(FIRST_NAME_SUBSCRIBER)
                .lastName(LAST_NAME_SUBSCRIBER)
                .email(EMAIL_SUBSCRIBER)
                .password(PASSWORD_SUBSCRIBER)
                .build();
    }

    public static UserRegisterRequest getRegisterRequestSubscriberWithIncorrectFirstName() {
        return UserRegisterRequest.builder()
                .firstName(FIRST_NAME_INCORRECT)
                .lastName(LAST_NAME_SUBSCRIBER)
                .email(EMAIL_SUBSCRIBER)
                .password(PASSWORD_SUBSCRIBER)
                .build();
    }

    public static UserRegisterRequest getRegisterRequestSubscriberWithIncorrectLastName() {
        return UserRegisterRequest.builder()
                .firstName(FIRST_NAME_SUBSCRIBER)
                .lastName(LAST_NAME_INCORRECT)
                .email(EMAIL_SUBSCRIBER)
                .password(PASSWORD_SUBSCRIBER)
                .build();
    }

    public static UserUpdateRequest getUserUpdateRequest() {
        return UserUpdateRequest.builder()
                .firstName(FIRST_NAME_SUBSCRIBER)
                .lastName(LAST_NAME_SUBSCRIBER)
                .email(EMAIL_SUBSCRIBER)
                .password(PASSWORD_SUBSCRIBER)
                .build();
    }

    public static UserResponse getUserResponse() {
        return UserResponse.builder()
                .id(ID_JOURNALIST)
                .createdBy(CREATED_BY_JOURNALIST)
                .updatedBy(UPDATED_BY_JOURNALIST)
                .createdAt(CREATED_AT_JOURNALIST)
                .updatedAt(UPDATED_AT_JOURNALIST)
                .firstName(FIRST_NAME_JOURNALIST)
                .lastName(LAST_NAME_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .email(EMAIL_JOURNALIST)
                .status(Status.ACTIVE)
                .roles(List.of(RoleTestData.getRoleResponse()))
                .build();
    }
}