package com.solbeg.userservice.util.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface UserJsonSupplier {

    static String getRequestJsonWithNotValidStatusForRegistrationUser() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/not_valid_status_for_registration_user.json"));
    }
}