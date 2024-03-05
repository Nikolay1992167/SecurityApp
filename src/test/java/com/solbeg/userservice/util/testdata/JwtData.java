package com.solbeg.userservice.util.testdata;

import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.ACCESS_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_JOURNALIST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.REFRESH_TOKEN;

@Data
@Builder(setterPrefix = "with")
public class JwtData {

    @Builder.Default
    private UUID id = ID_JOURNALIST;

    @Builder.Default
    private String email = EMAIL_JOURNALIST;

    @Builder.Default
    private String accessToken = ACCESS_TOKEN;

    @Builder.Default
    private String refreshToken = REFRESH_TOKEN;

    @Builder.Default
    private String password = PASSWORD_JOURNALIST;

    public JwtRequest getJwtRequest() {
        return JwtRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    public JwtRequest getJwtRequestForIT() {
        return JwtRequest.builder()
                .email(EMAIL_JOURNALIST_FOR_IT)
                .password(PASSWORD_JOURNALIST_FOR_IT)
                .build();
    }

    public JwtResponse getJwtResponse() {
        return JwtResponse.builder()
                .id(id)
                .email(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}