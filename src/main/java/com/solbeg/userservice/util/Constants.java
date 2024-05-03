package com.solbeg.userservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static String ROLE_JOURNALIST = "JOURNALIST";
    public static String ROLE_SUBSCRIBER = "SUBSCRIBER";
    public static String FIRST_NAME = "firstName";
    public static String LAST_NAME = "lastName";
    public static String URL_EMAIL_SERVICE = "/api/v1/send/email";
    public static String ACTIVATION_URL = "http://localhost:8081/api/v1/admin/activation?userToken=";
    public static String NAME_LINK = "activationLink";
}
