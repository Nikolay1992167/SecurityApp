package com.solbeg.userservice.util.initdata;

import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class InitData {

    public static final String JOURNALIST_FIRST_NAME = "Egor";
    public static final String SUBSCRIBER_FIRST_NAME = "Alex";
    public static final String FIRST_NAME_INCORRECT = "P";
    public static final String JOURNALIST_LAST_NAME = "Strelin";
    public static final String SUBSCRIBER_LAST_NAME = "Volk";
    public static final String LAST_NAME_INCORRECT = "S";

    public static final String EMAIL_ADMIN = "ivan@google.com";
    public static final String EMAIL_JOURNALIST = "strelin@mail.ru";
    public static final String EMAIL_SUBSCRIBER = "volk@google.com";
    public static final String EMAIL_INCORRECT = "jsjsdkfjsjdfkj123213";
    public static final String EMAIL_NOT_EXIST = "nikolay@mail.by";

    public static final String PASSWORD_ADMIN = "$2a$10$ch99apPuJoORMIf8Ew.D9e.cgWa1C6EYQ3iQMp7idTlGyNpyoF.P.";
    public static final String PASSWORD_JOURNALIST = "$2a$10$uuwsQHbWZMIUMTuxKwij8e/l5zea9.Q2XW0eG3Bs/2fUMarbqiymG";
    public static final String PASSWORD_SUBSCRIBER = "$2a$10$wH5b5g3QibOOdDhOVlSGxuyvqOO4kDcWMI3TQKNK9HdzUeQLowmNG";
    public static final String PASSWORD_INCORRECT = "1";

    public static final UUID ID_ADMIN = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
    public static final UUID ID_JOURNALIST = UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12");
    public static final UUID ID_SUBSCRIBER = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13");
    public static final UUID ID_INCORRECT = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a45");
    public static final UUID ID_NOT_EXIST = UUID.fromString("ddcf4b2b-3ee4-4927-9466-953aa27c8785");

    public static final String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJlbGluQG1haWwucnUiLCJpZCI6ImIwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMiIsInJvbGVzIjpbIkpPVVJOQUxJU1QiXSwiZXhwIjoxNzA4ODgwNDQ2fQ.BjnqDutQchHRwDAZJdXhzHKxXevhcaKgHX36OWbZTRmG_oeelnbvEXyYrtQ89CnDgygDkH5vgUcsHQccVEewYA";
    public static final String REFRESH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJlbGluQG1haWwucnUiLCJpZCI6ImIwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMiIsImV4cCI6MTcxMTQ2ODg0Nn0.dyEnMlSVVqCBb5oQFHXngGmu2l6jgn2o7rKXyB0jypb0DWCyRzcUbNWyCfW7if4OHUILT4AW0sbMpIVvQEUlFA";
    public static final String INCORRECT_TOKEN = "1fhgdfhfdghfghfhdf.2fghdfhdfhdfghfghgfhfghfgh.3.lkdkg;dlskg;sld;gdfkgl;kdf;lgd;fklg;dslkg;ldkg;";
    public static final String REFRESH_TOKEN_INCORRECT_SIGNATURE = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsImV4cCI6MTcxMTUyOTU3NX0.kVQbhEbEptpqx7Ps10Dfp-LvKVYf5RXabAWTnfK-AlBENalCFQ83pGQh8pYLqBqGLuIoxMuSa47G";
    public static final String TOKEN_ADMIN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsImV4cCI6MTcxMTU1MDg2MH0.qIhXeHWNVXxdMcTF6sjac5pQd4Wp2nBpF3d_f67Izla99p3QsiaiNf3Yj5xQGvIyNPmQOWp8g0d1XTtUJuOgNQ";

    public static final UUID CREATED_BY_JOURNALIST = null;
    public static final UUID UPDATED_BY_JOURNALIST = null;
    public static final UUID CREATED_BY_SUBSCRIBER = null;
    public static final UUID UPDATED_BY_SUBSCRIBER = null;

    public static final LocalDateTime CREATED_AT_JOURNALIST = LocalDateTime.of(2024, 2, 13, 12, 0, 0);
    public static final LocalDateTime UPDATED_AT_JOURNALIST = LocalDateTime.of(2024, 2, 16, 12, 0, 0);
    public static final LocalDateTime CREATED_AT_SUBSCRIBER = LocalDateTime.of(2024, 2, 11, 12, 0, 0);
    public static final LocalDateTime UPDATED_AT_SUBSCRIBER = LocalDateTime.of(2024, 2, 14, 12, 0, 0);

    public static final List<String> JOURNALIST_LIST_OF_ROLES = List.of("JOURNALIST");
    public static final List<String> SUBSCRIBER_LIST_OF_ROLES = List.of("SUBSCRIBER");

    public static final UUID ID_ROLE = UUID.fromString("2512c298-6a1d-48d7-a12d-b51069aceb08");
    public static final UUID CREATED_BY_ROLE = null;
    public static final UUID UPDATED_BY_ROLE = null;
    public static final LocalDateTime CREATED_AT_ROLE = LocalDateTime.of(2024, 2, 10, 12, 0, 0);
    public static final LocalDateTime UPDATED_AT_ROLE = LocalDateTime.of(2024, 2, 11, 12, 0, 0);
    public static final String ROLE_NAME_JOURNALIST = "JOURNALIST";
    public static final String ROLE_NAME_SUBSCRIBER = "SUBSCRIBER";

    public static final PageRequest DEFAULT_PAGE_REQUEST_FOR_IT = PageRequest.of(0, 15);

    public static final String SECRET_TEST = "73357638792F423F4528482B4D6251655468576D5A7133743677397A24432646";
    public static final Long ACCESS_TEST = 1L;
    public static final Long REFRESH_TEST = 1L;

    public static final String URL_AUTH = "/api/v1/auth";
    public static final String URL_USERS = "/api/v1/users";
}
