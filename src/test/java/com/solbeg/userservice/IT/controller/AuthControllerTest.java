package com.solbeg.userservice.IT.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.dto.response.UserRegisterResponse;
import com.solbeg.userservice.service.AuthService;
import com.solbeg.userservice.service.impl.UserServiceImpl;
import com.solbeg.userservice.util.PostgresSqlContainerInitializer;
import com.solbeg.userservice.util.json.UserJsonSupplier;
import com.solbeg.userservice.util.testdata.JwtData;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.solbeg.userservice.util.initdata.InitData.EMAIL_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.FIRST_NAME_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.LAST_NAME_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.REFRESH_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.SUBSCRIBER_FIRST_NAME;
import static com.solbeg.userservice.util.initdata.InitData.SUBSCRIBER_LAST_NAME;
import static com.solbeg.userservice.util.initdata.InitData.SUBSCRIBER_LIST_OF_ROLES;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
class AuthControllerTest extends PostgresSqlContainerInitializer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private AuthService authService;

    @Nested
    class AuthenticatePostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            JwtRequest request = JwtData.builder()
                    .build()
                    .getJwtRequest();
            JwtResponse response = JwtData.builder()
                    .build()
                    .getJwtResponse();

            when(authService.login(request))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(response.getId().toString()),
                            jsonPath("$.email").value(response.getEmail()),
                            jsonPath("$.accessToken").value(response.getAccessToken()),
                            jsonPath("$.refreshToken").value(response.getRefreshToken())
                    );
        }

        @Test
        void shouldReturnThrowExceptionAndStatus409() throws Exception {
            // given
            JwtRequest request = JwtData.builder()
                    .withEmail(EMAIL_INCORRECT)
                    .build()
                    .getJwtRequest();
            String json = UserJsonSupplier.getPatternEmailErrorResponse();

            // when, then
            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

//        @Test
//        void shouldReturnThrowExceptionAndStatus401() throws Exception {
//            // given
//            JwtRequest request = JwtData.builder()
//                    .withEmail(EMAIL_NOT_EXIST)
//                    .withPassword(PASSWORD_JOURNALIST)
//                    .build()
//                    .getJwtRequest();
//            String json = UserJsonSupplier.getNotFoundUserWithEmailResponse();
//
//            // when, then
//            mockMvc.perform(post("/api/v1/auth/authenticate")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isUnauthorized())
//                    .andExpect(content().json(json));
//        }
    }

    @Nested
    class RegisterJournalistPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus201() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .build()
                    .getUserRegisterRequest();
            UserRegisterResponse response = UserTestData.builder()
                    .build()
                    .getUserRegisterResponse();

            when(userService.registerJournalist(request))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(post("/api/v1/auth/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isCreated(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(response.getId().toString()),
                            jsonPath("$.firstName").value(response.getFirstName()),
                            jsonPath("$.lastName").value(response.getLastName()),
                            jsonPath("$.password").value(response.getPassword()),
                            jsonPath("$.email").value(response.getEmail()),
                            jsonPath("$.roles").value("JOURNALIST"),
                            jsonPath("$.status").value(response.getStatus().toString())
                    );
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus409WithNotValidFirstName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withFirstName(FIRST_NAME_INCORRECT)
                    .build()
                    .getUserRegisterRequest();
            String json = UserJsonSupplier.getNotValidFirstNameForRegistrationUser();

            // when, then
            mockMvc.perform(post("/api/v1/auth/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus409WithNotValidLastName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withLastName(LAST_NAME_INCORRECT)
                    .build()
                    .getUserRegisterRequest();
            String json = UserJsonSupplier.getNotValidLastNameForRegistrationUser();

            // when, then
            mockMvc.perform(post("/api/v1/auth/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus409WithNotValidPassword() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withPassword(PASSWORD_INCORRECT)
                    .build()
                    .getUserRegisterRequest();
            String json = UserJsonSupplier.getNotValidPasswordForRegistrationUser();

            // when, then
            mockMvc.perform(post("/api/v1/auth/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus409WithNotValidEmail() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withEmail(EMAIL_INCORRECT)
                    .build()
                    .getUserRegisterRequest();
            String json = UserJsonSupplier.getNotValidEmailForRegistrationUser();

            // when, then
            mockMvc.perform(post("/api/v1/auth/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus409WithNotValidStatus() throws Exception {
            // given
            String request = UserJsonSupplier.getRequestJsonWithNotValidStatusForRegistrationUser();
            String json = UserJsonSupplier.getNotValidStatusForRegistrationUser();

            // when, then
            mockMvc.perform(post("/api/v1/auth/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }
    }

    @Nested
    class RegisterSubscriberPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus201() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withFirstName(SUBSCRIBER_FIRST_NAME)
                    .withLastName(SUBSCRIBER_LAST_NAME)
                    .withEmail(EMAIL_SUBSCRIBER)
                    .withPassword(PASSWORD_SUBSCRIBER)
                    .build()
                    .getUserRegisterRequest();
            UserRegisterResponse response = UserTestData.builder()
                    .withFirstName(SUBSCRIBER_FIRST_NAME)
                    .withLastName(SUBSCRIBER_LAST_NAME)
                    .withEmail(EMAIL_SUBSCRIBER)
                    .withPassword(PASSWORD_SUBSCRIBER)
                    .withRoles(SUBSCRIBER_LIST_OF_ROLES)
                    .build()
                    .getUserRegisterResponse();

            when(userService.registerSubscriber(request))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(post("/api/v1/auth/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isCreated(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(response.getId().toString()),
                            jsonPath("$.firstName").value(response.getFirstName()),
                            jsonPath("$.lastName").value(response.getLastName()),
                            jsonPath("$.password").value(response.getPassword()),
                            jsonPath("$.email").value(response.getEmail()),
                            jsonPath("$.roles").value("SUBSCRIBER"),
                            jsonPath("$.status").value(response.getStatus().toString())
                    );
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus409WithNotValidFirstName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withFirstName(FIRST_NAME_INCORRECT)
                    .build()
                    .getUserRegisterRequest();
            String json = UserJsonSupplier.getNotValidFirstNameForRegistrationUser();

            // when, then
            mockMvc.perform(post("/api/v1/auth/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus409WithNotValidLastName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withLastName(LAST_NAME_INCORRECT)
                    .build()
                    .getUserRegisterRequest();
            String json = UserJsonSupplier.getNotValidLastNameForRegistrationUser();

            // when, then
            mockMvc.perform(post("/api/v1/auth/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus409WithNotValidPassword() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withPassword(PASSWORD_INCORRECT)
                    .build()
                    .getUserRegisterRequest();
            String json = UserJsonSupplier.getNotValidPasswordForRegistrationUser();

            // when, then
            mockMvc.perform(post("/api/v1/auth/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus409WithNotValidEmail() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withEmail(EMAIL_INCORRECT)
                    .build()
                    .getUserRegisterRequest();
            String json = UserJsonSupplier.getNotValidEmailForRegistrationUser();

            // when, then
            mockMvc.perform(post("/api/v1/auth/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus409WithNotValidStatus() throws Exception {
            // given
            String request = UserJsonSupplier.getRequestJsonWithNotValidStatusForRegistrationUser();
            String json = UserJsonSupplier.getNotValidStatusForRegistrationUser();

            // when, then
            mockMvc.perform(post("/api/v1/auth/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }
    }

    @Nested
    class RefreshTokenPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            JwtResponse response = JwtData.builder()
                    .build()
                    .getJwtResponse();

            when(authService.refresh(REFRESH_TOKEN))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(post("/api/v1/auth/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(REFRESH_TOKEN)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(response.getId().toString()),
                            jsonPath("$.email").value(response.getEmail()),
                            jsonPath("$.accessToken").value(response.getAccessToken()),
                            jsonPath("$.refreshToken").value(response.getRefreshToken())
                    );
        }

//        @Test
//        void shouldReturnReturnThrowExceptionAndStatus409WithNotValidRefreshToken() throws Exception {
//            // given
//            RefreshTokenRequest request = JwtData.builder()
//                    .withRefreshToken(INCCORECT_TOKEN)
//                    .build()
//                    .getRefreshTokenRequest();
//            String json = UserJsonSupplier.getIncorrectRefreshTokenResponse();
//
//            // when, then
//            mockMvc.perform(post("/api/v1/auth/refresh")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isConflict())
//                    .andExpect(content().json(json));
//        }

//        @Test
//        void shouldReturnReturnThrowExceptionAndStatus500WithIncorrectJWTSignature() throws Exception {
//            // given
//            RefreshTokenRequest request = JwtData.builder()
//                    .withRefreshToken(REFRESH_TOKEN_INCORRECT_SIGNATURE)
//                    .build()
//                    .getRefreshTokenRequest();
//            String json = UserJsonSupplier.getRefreshTokenWithIncorrectJWTSignatureResponse();
//
//            // when, then
//            mockMvc.perform(post("/api/v1/auth/refresh")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isInternalServerError())
//                    .andExpect(content().json(json));
//        }
    }
}