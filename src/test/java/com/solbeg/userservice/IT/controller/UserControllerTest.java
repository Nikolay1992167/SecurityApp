package com.solbeg.userservice.IT.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solbeg.userservice.security.jwt.JwtTokenProvider;
import com.solbeg.userservice.service.impl.UserServiceImpl;
import com.solbeg.userservice.util.PostgresSqlContainerInitializer;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.URL_USERS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerTest extends PostgresSqlContainerInitializer {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @SpyBean
    private UserServiceImpl userService;

    @Test
    void shouldReturnExpectedValueAndStatus200() throws Exception {
        // given
        String userToken = jwtTokenProvider.createRefreshToken(ID_JOURNALIST_FOR_IT, EMAIL_JOURNALIST_FOR_IT);

        // when, then
        mockMvc.perform(post(URL_USERS + "/details")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToken)))
                .andExpect(
                        status().isOk());
    }
}