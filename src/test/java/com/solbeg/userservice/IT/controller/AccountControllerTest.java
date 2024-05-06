package com.solbeg.userservice.IT.controller;

import com.solbeg.userservice.security.jwt.JwtTokenProvider;
import com.solbeg.userservice.util.PostgresSqlContainerInitializer;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.solbeg.userservice.util.initdata.InitData.BEARER;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.URL_USERS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class AccountControllerTest extends PostgresSqlContainerInitializer {
    private final MockMvc mockMvc;
    private final JwtTokenProvider jwtTokenProvider;

    @Test
    void shouldReturnExpectedValueAndStatus200() throws Exception {
        // given
        String userToken = jwtTokenProvider.createRefreshToken(ID_JOURNALIST_FOR_IT, EMAIL_JOURNALIST_FOR_IT);

        // when, then
        mockMvc.perform(get(URL_USERS + "/details")
                        .header(AUTHORIZATION, BEARER + userToken)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}