package com.solbeg.userservice.enums.error_response;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    ERROR_PARSING("Error parsing the JWT token."),
    USER_NOT_FOUND("User not found with "),
    USER_NOT_EXIST_OR_NOT_ACTIVE("User is not exist or not active with "),
    USER_NOT_EXIST("User is not exist with "),
    UNIQUE_USER_EMAIL("Email is occupied! Another user is already registered by this "),
    CHANGE_STATUS("You cannot change the status of a user with the ADMIN role."),
    USER_NOT_ACTIVE("User is not active!"),

    USERTOKEN_NOT_FOUND("UserToken not found with token "),
    ERROR_SEND_DATA("Failed to send user data to mail-service."),
    TOKEN_EXPIRED("Token expired!");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}