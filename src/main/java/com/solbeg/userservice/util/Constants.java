package com.solbeg.userservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static String ROLE_JOURNALIST = "JOURNALIST";
    public static String ROLE_SUBSCRIBER = "SUBSCRIBER";
    public static String ACTIVATION_URL = "http://localhost:8081/api/v1/admin/activation?userToken=";
    public static String NAME_LINK = "activationLink";
    public static String DLQ_EXCHANGE = "deadLetterExchange";
    public static String DLQ_ROUTING_KEY = "deadLetter";
    public static String DLQ_NAME = "deadLetter-queue";
    public static String ACTIVATION_QUEUE_NAME = "activation-queue";
    public static String INFORMATION_QUEUE_NAME = "information-queue";
    public static String ARG_LENGTH_QUEUE = "x-max-length";
    public static String ARG_EXCHANGE_DLQ = "x-dead-letter-exchange";
    public static String ARG_ROUTING_KEY_DLQ = "x-dead-letter-routing-key";
    public static String ROUTING_KEY_ACTIVATION = "activation";
    public static String ROUTING_KEY_INFORMATION = "information";
}
