package com.solbeg.userservice.util.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface UserJsonSupplier {

    static String getPatternEmailErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/email_incorrect.json"));
    }

    static String getNotFoundUserWithEmailResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/401_uzer_with_email_not_exist.json"));
    }

    static String getNotValidFirstNameForRegistrationUser() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/not_valid_firstname_for_registration_user.json"));
    }

    static String getNotValidLastNameForRegistrationUser() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/not_valid_lastname_for_registration_user.json"));
    }

    static String getNotValidPasswordForRegistrationUser() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/not_valid_password_for_registration_user.json"));
    }

    static String getNotValidEmailForRegistrationUser() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/not_valid_email_for_registration_user.json"));
    }

    static String getRequestJsonWithNotValidStatusForRegistrationUser() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/not_valid_status_for_registration_user.json"));
    }

    static String getNotValidStatusForRegistrationUser() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/not_valid_status_for_registration_user.json"));
    }

    static String getIncorrectRefreshTokenResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/incorrect_token_response.json"));
    }

    static String getRefreshTokenWithIncorrectJWTSignatureResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/refresh_token_with_incorrect_jwt_signature.json"));
    }
}