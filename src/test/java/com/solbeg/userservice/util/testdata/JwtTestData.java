package com.solbeg.userservice.util.testdata;

import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.response.JwtResponse;

import static com.solbeg.userservice.util.initdata.InitData.ACCESS_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_NOT_EXIST;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_JOURNALIST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.REFRESH_TOKEN;

public class JwtTestData {

    public static JwtRequest getJwtRequest() {
        return JwtRequest.builder()
                .email(EMAIL_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .build();
    }

    public static JwtRequest getJwtRequestWithIncorrectEmail() {
        return JwtRequest.builder()
                .email(EMAIL_INCORRECT)
                .password(PASSWORD_JOURNALIST)
                .build();
    }

    public static JwtRequest getJwtRequestWithIncorrectEnteredDate() {
        return JwtRequest.builder()
                .email(EMAIL_INCORRECT)
                .password(PASSWORD_INCORRECT)
                .build();
    }

    public static JwtRequest getJwtRequestWithEmailNotExist() {
        return JwtRequest.builder()
                .email(EMAIL_NOT_EXIST)
                .password(PASSWORD_JOURNALIST)
                .build();
    }

    public static JwtRequest getJwtRequestForIT() {
        return JwtRequest.builder()
                .email(EMAIL_JOURNALIST_FOR_IT)
                .password(PASSWORD_JOURNALIST_FOR_IT)
                .build();
    }

    public static JwtResponse getJwtResponse() {
        return JwtResponse.builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .build();
    }
}