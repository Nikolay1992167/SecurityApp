package com.solbeg.userservice.IT.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.request.RefreshTokenRequest;
import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.security.jwt.JwtTokenProvider;
import com.solbeg.userservice.util.PostgresSqlContainerInitializer;
import com.solbeg.userservice.util.testdata.JwtData;
import com.solbeg.userservice.util.testdata.UserTestData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static com.solbeg.userservice.util.initdata.InitData.EMAIL_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_NOT_EXIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.INCORRECT_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.URL_AUTH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AuthControllerTest extends PostgresSqlContainerInitializer {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8088);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    class AuthenticatePostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            JwtRequest request = JwtData.getJwtRequestForIT();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.id").isNotEmpty(),
                            jsonPath("$.email").value(request.getEmail()),
                            jsonPath("$.accessToken").isString(),
                            jsonPath("$.refreshToken").isString());
        }

        @Test
        void shouldReturnThrowExceptionAndStatus400WhenEmailIncorrect() throws Exception {
            // given
            JwtRequest request = JwtData.getJwtRequestWithIncorrectEmail();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message")
                            .value("{email=must be a well-formed email address}"));
        }

        @Test
        void shouldReturnThrowExceptionAndStatus400WhenEmailNotExist() throws Exception {
            // given
            JwtRequest request = JwtData.getJwtRequestWithEmailNotExist();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message")
                            .value(ErrorMessage.USER_NOT_EXIST.getMessage() + EMAIL_NOT_EXIST));
        }
    }

    @Nested
    class RegisterJournalistPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus201() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestJournalist();

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200));

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/journalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isCreated()
                    );
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidFirstName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestJournalistWithIncorrectFirstName();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/journalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message")
                            .value("{firstName=size must be between 2 and 40}"));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidLastName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestJournalistWithIncorrectLastName();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/journalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message")
                            .value("{lastName=size must be between 2 and 50}"));
        }
    }

    @Nested
    class RegisterSubscriberPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus201() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestSubscriber();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/subscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isCreated()
                    );
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidFirstName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestSubscriberWithIncorrectFirstName();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/subscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message")
                            .value("{firstName=size must be between 2 and 40}"));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidLastName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestSubscriberWithIncorrectLastName();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/subscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message")
                            .value("{lastName=size must be between 2 and 50}"));
        }
    }

    @Nested
    class RefreshTokenPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            String token = jwtTokenProvider.createRefreshToken(ID_ADMIN, EMAIL_ADMIN);
            RefreshTokenRequest request = new RefreshTokenRequest(token);

            // when, then
            mockMvc.perform(post(URL_AUTH + "/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isOk()
                    );
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidRefreshToken() throws Exception {
            // given
            RefreshTokenRequest request = new RefreshTokenRequest(INCORRECT_TOKEN);

            // when, then
            mockMvc.perform(post(URL_AUTH + "/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }
}