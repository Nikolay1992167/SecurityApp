package com.solbeg.userservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String ROLE_JOURNALIST = "JOURNALIST";
    public static final String ROLE_SUBSCRIBER = "SUBSCRIBER";
    public static final String ACTIVATION_URL = "http://localhost:8081/api/v1/admin/activation?userToken=";
    public static final String NAME_LINK = "activationLink";
    public static final String DLQ_EXCHANGE = "deadLetterExchange";
    public static final String DLQ_ROUTING_KEY = "deadLetter";
    public static final String DLQ_NAME = "deadLetter-queue";
    public static final String ACTIVATION_QUEUE_NAME = "activation-queue";
    public static final String INFORMATION_QUEUE_NAME = "information-queue";
    public static final String ARG_LENGTH_QUEUE = "x-max-length";
    public static final String ARG_EXCHANGE_DLQ = "x-dead-letter-exchange";
    public static final String ARG_ROUTING_KEY_DLQ = "x-dead-letter-routing-key";
    public static final String ROUTING_KEY_ACTIVATION = "activation";
    public static final String ROUTING_KEY_INFORMATION = "information";
    public static final String EMAIL_ERROR = "Incorrect email format!";
    public static final String SIZE_PASSWORD_ERROR = "The length of the data should be from 3 to 100 characters!";
    public static final String SIZE_NAME_ERROR = "The length of the data should be from 2 to 50 characters!";
}
