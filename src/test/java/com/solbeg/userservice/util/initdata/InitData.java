package com.solbeg.userservice.util.initdata;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class InitData {

    public static String FIRST_NAME_ADMIN = "Alex";
    public static String FIRST_NAME_JOURNALIST = "Vlad";
    public static String FIRST_NAME_SUBSCRIBER = "Olga";
    public static String FIRST_NAME_INCORRECT = "P";
    public static String LAST_NAME_ADMIN = "Dronov";
    public static String LAST_NAME_JOURNALIST = "Suzko";
    public static String LAST_NAME_SUBSCRIBER = "Pronina";
    public static String LAST_NAME_INCORRECT = "S";

    public static String EMAIL_ADMIN = "dronov@google.com";
    public static String EMAIL_JOURNALIST = "suzko@mail.ru";
    public static String EMAIL_SUBSCRIBER = "pronina@google.com";
    public static String EMAIL_INCORRECT = "incorrect";
    public static String EMAIL_NOT_EXIST = "nikolay@mail.by";

    public static String PASSWORD_ADMIN = "$2a$10$3z6TvAEO2fwLedqVihxZDeQQM.BFCqIc5fP78YUUlL8BcVaPUGHKO";
    public static String PASSWORD_JOURNALIST = "$2a$10$myqg0IzeeQH.6kZWEDhwv.bG6vg0iuwGyB6eJOwEgu5yiboXAGXgG";
    public static String PASSWORD_SUBSCRIBER = "$2a$10$Vt4OsDqKYVck85vLU3Sv7.ERZ17ZtnpVkC4YZOD.XB8zlxT9VEfxi";
    public static String PASSWORD_INCORRECT = "1";

    public static UUID ID_ADMIN = UUID.fromString("44212253-a305-4495-9982-45e833aa74ac");
    public static UUID ID_JOURNALIST = UUID.fromString("f2361e91-718e-41ad-9ddc-4be05ebc09b5");
    public static UUID ID_SUBSCRIBER = UUID.fromString("42f77a57-3cd7-4e5d-a2f8-78f951fc9a41");
    public static UUID ID_INCORRECT = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a45");
    public static UUID ID_NOT_EXIST = UUID.fromString("ddcf4b2b-3ee4-4927-9466-953aa27c8785");

    public static String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJlbGluQG1haWwucnUiLCJpZCI6ImIwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMiIsInJvbGVzIjpbIkpPVVJOQUxJU1QiXSwiZXhwIjoxNzA4ODgwNDQ2fQ.BjnqDutQchHRwDAZJdXhzHKxXevhcaKgHX36OWbZTRmG_oeelnbvEXyYrtQ89CnDgygDkH5vgUcsHQccVEewYA";
    public static String REFRESH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJlbGluQG1haWwucnUiLCJpZCI6ImIwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMiIsImV4cCI6MTcxMTQ2ODg0Nn0.dyEnMlSVVqCBb5oQFHXngGmu2l6jgn2o7rKXyB0jypb0DWCyRzcUbNWyCfW7if4OHUILT4AW0sbMpIVvQEUlFA";
    public static String INCORRECT_TOKEN = "1fhgdfhfdghfghfhdf.2fghdfhdfhdfghfghgfhfghfgh.3.lkdkg;dlskg;sld;gdfkgl;kdf;lgd;fklg;dslkg;ldkg;";
    public static String TOKEN_INCORRECT_SIGNATURE = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsImV4cCI6MTcxMTUyOTU3NX0.kVQbhEbEptpqx7Ps10Dfp-LvKVYf5RXabAWTnfK-AlBENalCFQ83pGQh8pYLqBqGLuIoxMuSa47G";
    public static String TOKEN_ADMIN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsImV4cCI6MTcxMTU1MDg2MH0.qIhXeHWNVXxdMcTF6sjac5pQd4Wp2nBpF3d_f67Izla99p3QsiaiNf3Yj5xQGvIyNPmQOWp8g0d1XTtUJuOgNQ";

    public static UUID CREATED_BY_JOURNALIST = null;
    public static UUID UPDATED_BY_JOURNALIST = UUID.fromString("44212253-a305-4495-9982-45e833aa74ac");
    public static UUID CREATED_BY_SUBSCRIBER = null;
    public static UUID UPDATED_BY_SUBSCRIBER = UUID.fromString("44212253-a305-4495-9982-45e833aa74ac");

    public static LocalDateTime CREATED_AT_JOURNALIST = LocalDateTime.of(2024, 1, 2, 12, 0, 0);
    public static LocalDateTime UPDATED_AT_JOURNALIST = LocalDateTime.of(2024, 1, 3, 12, 0, 0);
    public static LocalDateTime CREATED_AT_SUBSCRIBER = LocalDateTime.of(2024, 1, 4, 12, 0, 0);
    public static LocalDateTime UPDATED_AT_SUBSCRIBER = LocalDateTime.of(2024, 1, 5, 12, 0, 0);

    public static List<String> JOURNALIST_LIST_OF_ROLES = List.of("JOURNALIST");
    public static List<String> SUBSCRIBER_LIST_OF_ROLES = List.of("SUBSCRIBER");

    public static UUID ID_ROLE = UUID.fromString("2512c298-6a1d-48d7-a12d-b51069aceb08");
    public static UUID CREATED_BY_ROLE = null;
    public static UUID UPDATED_BY_ROLE = null;
    public static LocalDateTime CREATED_AT_ROLE = LocalDateTime.of(2024, 2, 10, 12, 0, 0);
    public static LocalDateTime UPDATED_AT_ROLE = LocalDateTime.of(2024, 2, 11, 12, 0, 0);
    public static String ROLE_NAME_JOURNALIST = "JOURNALIST";
    public static String ROLE_NAME_SUBSCRIBER = "SUBSCRIBER";

    public static PageRequest DEFAULT_PAGE_REQUEST_FOR_IT = PageRequest.of(0, 15);

    public static String SECRET_TEST = "73357638792F423F4528482B4D6251655468576D5A7133743677397A24432646";
    public static Long ACCESS_TEST = 1L;
    public static Long REFRESH_TEST = 1L;

    public static String URL_AUTH = "/api/v1/auth";
    public static String URL_USERS = "/api/v1/users";
}
